package buzz.song.facilitator.service;

import buzz.song.facilitator.model.*;
import buzz.song.facilitator.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class VenueService {

	private final VenueRepository venueRepo;
	private final VenueStatsRepository statRepo;
	private final TrackRepository trackRepo;
	private final VoteRepository voteRepo;
	private final VoteSkipRepository voteSkipRepository;

	@Value("${venue.voteskip_threshold}")
	private int voteskipThreshold;

	@Autowired
	public VenueService(
			final VenueRepository venueRepo,
			final VenueStatsRepository statRepo,
			final TrackRepository trackRepo,
			final VoteRepository voteRepo,
			final VoteSkipRepository voteSkipRepository
	) {
		this.venueRepo = venueRepo;
		this.statRepo = statRepo;
		this.trackRepo = trackRepo;
		this.voteRepo = voteRepo;
		this.voteSkipRepository = voteSkipRepository;
	}

	/**
	 * Creates a {@link Venue} at the given coordinates
	 *
	 * @param latitude  latitutde of GPS location
	 * @param longitude longitude of GPS location
	 * @return Future for created {@link Venue}
	 */
	public CompletableFuture<Venue> createVenue(final String name, final String hostName, final String hostId, final double latitude, final double longitude) {
		final String venueId = UUID.randomUUID().toString();
		final Venue venue = new Venue(
				venueId,
				name,
				hostName,
				hostId,
				new HashSet<>(),
				null,
				new TreeSet<>(),
				latitude,
				longitude,
				new VenueStats(venueId, 0, 0, 0, 0, 0)
		);
		return CompletableFuture.supplyAsync(() -> venueRepo.save(venue));
	}

	/**
	 * Pops the top track from the playlist and sets it as {@link Venue} current track
	 *
	 * @param venueId {@link Venue} with ID to get next track for
	 * @return updated {@link Venue}
	 */
	public CompletableFuture<Venue> venueNextTrack(final String venueId) {
		return CompletableFuture.supplyAsync(() -> {
			final Venue venue = venueRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find venue '" + venueId + "'"));
			if (!venue.getPlaylist().isEmpty()) {
				final Track first = venue.getPlaylist().first();
				venue.getPlaylist().remove(first);
				venue.setCurrentTrackId(first.getTrackId());

				final VenueStats stats = venue.getVenueStats();
				stats.setTotalTracksPlayed(stats.getTotalTracksPlayed() + 1);
				venue.setVenueStats(stats);
			} else {
				venue.setCurrentTrackId(null);
			}
			venue.setTimeProgress(0);
			venue.setTotalTime(0);
			venue.getVoteSkips().clear();
			return venueRepo.save(venue);
		});
	}

	/**
	 * Updates the current time and total time of a {@link Venue}'s current {@link Track}
	 *
	 * @param venueId     venue ID to update for
	 * @param timeProgress current time of track
	 * @param totalTime   total time of song
	 * @return Future representing action
	 */
	public CompletableFuture<Void> venueUpdateTime(final String venueId, final double timeProgress, final double totalTime) {
		return CompletableFuture.runAsync(() -> venueRepo.updateTimeProgress(venueId, timeProgress, totalTime));
	}

	/**
	 * Searches for nearby {@link Venue} instances.
	 *
	 * @param latitude  latitude to search
	 * @param longitude longitude to search near
	 * @return nearby {@link Venue} instances
	 */
	public CompletableFuture<Map<Venue, Double>> listVenues(final double latitude, final double longitude) {
		return CompletableFuture.supplyAsync(() -> venueRepo.findAll().stream().map(venue -> {
			final double otherLatitude = venue.getLatitude();
			final double otherLongitude = venue.getLongitude();

			final double dist = latitude == otherLatitude && longitude == otherLongitude ? 0 :
					Math.toDegrees(
							Math.acos(
									Math.sin(Math.toRadians(otherLatitude)) * Math.sin(Math.toRadians(latitude))
											+ Math.cos(Math.toRadians(otherLatitude)) * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(otherLongitude - longitude))
							)
					) * 60 * 1.1515;
			return new AbstractMap.SimpleEntry<>(venue, dist);
		}).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
	}

	/**
	 * Vote for a proposed {@link Track}
	 *
	 * @param venueId {@link Venue} to be voting for
	 * @param trackId {@link Track} in venue to vote for
	 * @param userId  ID of user voting
	 * @param upvote  True if upvote, false if downvote
	 * @return created {@link Vote}
	 */
	public CompletableFuture<Vote> voteForTrack(final String venueId, final String trackId, final String userId, final boolean upvote) {
		final Vote vote = new Vote(
				venueId,
				trackId,
				userId,
				upvote
		);
		return CompletableFuture.supplyAsync(() -> {
			final VenueStats stats = statRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find stats for venue '" + venueId + "'"));
			if (upvote) {
				stats.setTotalTrackUpvotes(stats.getTotalTrackUpvotes() + 1);
			} else {
				stats.setTotalTrackDownvotes(stats.getTotalTrackDownvotes() + 1);
			}
			statRepo.save(stats);
			return voteRepo.save(vote);
		});
	}

	/**
	 * Vote to skip track on behalf of a user
	 *
	 * @param venueId {@link Venue} to vote to skip current track for
	 * @param userId  user to voteskip on behalf of
	 * @return created {@link VoteSkip} object Future
	 */
	public CompletableFuture<VoteSkip> voteskipTrack(final String venueId, final String userId) {
		final VoteSkip voteSkip = new VoteSkip(venueId, userId);
		return CompletableFuture.supplyAsync(() -> {
			final VoteSkip created = voteSkipRepository.save(voteSkip);
			final Venue venue = venueRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find venue '" + venueId + "'"));
			if (venue.getVoteSkips().size() >= voteskipThreshold) {
				venueNextTrack(venueId).join();

				final VenueStats stats = statRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find stats for venue '" + venueId + "'"));
				stats.setTotalTracksSkipped(stats.getTotalTracksSkipped() + 1);
				statRepo.save(stats);
			}
			return created;
		});

	}

	/**
	 * Removes a user's voteskip for current track in a venue
	 *
	 * @param venueId {@link Venue} to remove voteskip for
	 * @param userId  User to remove voteskip on behalf of
	 * @return Future
	 */
	public CompletableFuture<Void> unvoteskipTrack(final String venueId, final String userId) {
		final VoteSkip.VoteSkipID id = new VoteSkip.VoteSkipID(venueId, userId);
		return CompletableFuture.runAsync(() -> voteSkipRepository.deleteById(id));
	}

	/**
	 * Removes a user vote for a track
	 *
	 * @param venueId {@link Venue} to remove vote for
	 * @param trackId {@link Track} to remove vote from
	 * @param userId  User to remove vote on behalf of
	 * @return Future
	 */
	public CompletableFuture<Void> unvoteForTrack(final String venueId, final String trackId, final String userId) {
		final Vote.VoteID id = new Vote.VoteID(venueId, trackId, userId);
		return CompletableFuture.runAsync(() -> voteRepo.deleteById(id));
	}

	/**
	 * Fetches a {@link Venue} by ID.
	 *
	 * @param venueId ID of {@link Venue} to fetch
	 * @return Future for fetched {@link Venue}
	 */
	public CompletableFuture<Venue> fetchVenue(final String venueId) {
		return CompletableFuture.supplyAsync(() -> venueRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find venue '" + venueId + "'")));
	}

	/**
	 * Marks a venue as closed
	 * @param venueId ID of {@link Venue} to close
	 * @return Future for closed {@link Venue}
	 */
	public CompletableFuture<Venue> closeVenue(final String venueId) {
		return CompletableFuture.supplyAsync(() -> {
			final Venue venue = venueRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find venue '" + venueId + "'"));
			venue.setClosedOn(new Timestamp(System.currentTimeMillis()));
			return venueRepo.save(venue);
		});
	}

	/**
	 * Adds a song to the {@link Venue} identified by the given venue ID.
	 *
	 * @param venueId the {@link Venue} ID to add a track to
	 * @param trackId the track ID to propose
	 * @return Future for result of proposal
	 */
	public CompletableFuture<Track> proposeTrack(final String venueId, final String trackId) {
		final Track track = new Track(
				venueId,
				trackId,
				new HashSet<>()
		);
		return CompletableFuture.supplyAsync(() -> {
			final VenueStats stats = statRepo.findById(venueId).orElseThrow(() -> new RuntimeException("Unable to find stats for venue '" + venueId + "'"));
			stats.setTotalTracksProposed(stats.getTotalTracksProposed() + 1);
			statRepo.save(stats);
			return trackRepo.save(track);
		});
	}
}
