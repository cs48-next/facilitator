package buzz.song.facilitator.service;

import buzz.song.facilitator.model.Track;
import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.Vote;
import buzz.song.facilitator.repository.TrackRepository;
import buzz.song.facilitator.repository.VenueRepository;
import buzz.song.facilitator.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class VenueService {

	private final VenueRepository venueRepo;
	private final TrackRepository trackRepo;
	private final VoteRepository voteRepo;

	@Autowired
	public VenueService(final VenueRepository venueRepo, final TrackRepository trackRepo, final VoteRepository voteRepo) {
		this.venueRepo = venueRepo;
		this.trackRepo = trackRepo;
		this.voteRepo = voteRepo;
	}

	/**
	 * Creates a {@link Venue} at the given coordinates
	 *
	 * @param latitude  latitutde of GPS location
	 * @param longitude longitude of GPS location
	 * @return Future for created {@link Venue}
	 */
	public CompletableFuture<Venue> createVenue(final String name, final String hostName, final String hostId, final double latitude, final double longitude) {
		final Venue venue = new Venue(
				UUID.randomUUID().toString(),
				name,
				hostName,
				hostId,
				null,
				new TreeSet<>(),
				latitude,
				longitude
		);
		return CompletableFuture.supplyAsync(() -> venueRepo.save(venue));
	}

	/**
	 * Pops the top track from the playlist and sets it as {@link Venue} current track
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
			} else {
				venue.setCurrentTrackId(null);
			}

			return venueRepo.save(venue);
		});
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
		return CompletableFuture.supplyAsync(() -> voteRepo.save(vote));
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
		return CompletableFuture.supplyAsync(() -> trackRepo.save(track));
	}
}
