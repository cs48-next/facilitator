package buzz.song.facilitator.controller;

import buzz.song.facilitator.model.Track;
import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.Vote;
import buzz.song.facilitator.model.request.VenueCreateRequest;
import buzz.song.facilitator.model.response.VenueListResponse;
import buzz.song.facilitator.service.VenueService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Entry point for API requests related to venues.
 */
@RestController
@CrossOrigin
public class VenueController {
	private static final Logger logger = LoggerFactory.getLogger(VenueController.class);

	@Value("${controllers.timeout_ms}")
	private Long controllerTimeout;

	private final VenueService venueService;

	@Autowired
	public VenueController(final VenueService venueService) {
		this.venueService = venueService;
	}

	@PostMapping("/venue")
	public DeferredResult<Venue> createVenue(
			@RequestBody final VenueCreateRequest createRequest
	) {
		final String name = createRequest.getVenueName();
		final String hostName = createRequest.getHostName();
		final String hostId = createRequest.getHostId();
		final double latitude = createRequest.getLatitude();
		final double longitude = createRequest.getLongitude();

		logger.info("Received request to create venue '{}' for '{}'(id: {}) at lat: {}, long: {}", name, hostName, hostId, latitude, longitude);

		final DeferredResult<Venue> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.createVenue(name, hostName, hostId, latitude, longitude).whenCompleteAsync((venue, ex) -> {
			if (ex != null) {
				logger.error("Received error when creating venue", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully created venue {}", venue.getId());
				responseDeferred.setResult(venue);
			}
		});

		return responseDeferred;
	}

	@GetMapping("/venue")
	public DeferredResult<VenueListResponse> listVenues(
			@RequestParam("latitude") final double latitude,
			@RequestParam("longitude") final double longitude
	) {
		logger.info("Received request to list venues near lat: {}, long: {}", latitude, longitude);

		final DeferredResult<VenueListResponse> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		final long now = System.currentTimeMillis();

		venueService.listVenues(latitude, longitude).whenCompleteAsync((venues, ex) -> {
			if (ex != null) {
				logger.error("Received error when listing venues", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				final VenueListResponse listing = new VenueListResponse(
						venues.entrySet().stream()
								.map(entry -> new VenueListResponse.VenueListing(
										entry.getKey().getId(),
										entry.getKey().getName(),
										entry.getKey().getHostName(),
										entry.getValue(),
										now - Optional.ofNullable(entry.getKey().getCreatedOn()).map(Timestamp::getTime).orElse(now + 1)
								)).collect(Collectors.toCollection(TreeSet::new))
				);
				logger.info("Found {} venues", venues.size());
				responseDeferred.setResult(listing);
			}
		});

		return responseDeferred;
	}

	@GetMapping("/venue/{venue_id}")
	public DeferredResult<Venue> fetchVenue(
			@PathVariable("venue_id") final String venueId
	) {
		Validate.notBlank(venueId);
		logger.info("Received request to fetch venue {}", venueId);

		final DeferredResult<Venue> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.fetchVenue(venueId).whenCompleteAsync((venue, ex) -> {
			if (ex != null) {
				logger.error("Received error when fetching venue", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully fetched venue {}", venue.getId());
				responseDeferred.setResult(venue);
			}
		});
		return responseDeferred;
	}

	@PostMapping("/venue/{venue_id}/next")
	public DeferredResult<Venue> nextTrack(
			@PathVariable("venue_id") final String venueId
	) {
		Validate.notBlank(venueId);
		logger.info("Received request to get next track for venue {}", venueId);

		final DeferredResult<Venue> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.venueNextTrack(venueId).whenCompleteAsync((venue, ex) -> {
			if (ex != null) {
				logger.error("Received error when updating venue track", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully updated venue track {}", venue);
				responseDeferred.setResult(venue);
			}
		});
		return responseDeferred;
	}


	@PutMapping("/track/{venue_id}/{track_id}")
	public DeferredResult<Track> proposeTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId
	) {
		Validate.notBlank(venueId);
		Validate.notBlank(trackId);
		logger.info("Received request to propose track {} to venue {}", trackId, venueId);

		final DeferredResult<Track> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.proposeTrack(venueId, trackId).whenCompleteAsync((track, ex) -> {
			if (ex != null) {
				logger.error("Received error when proposing track", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully proposed track {} to venue {}", trackId, venueId);
				responseDeferred.setResult(track);
			}
		});

		return responseDeferred;
	}

	@PutMapping("/vote/{venue_id}/{track_id}/{user_id}/upvote")
	public DeferredResult<Vote> upvoteTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId,
			@PathVariable("user_id") final String userId
	) {
		Validate.notBlank(venueId);
		Validate.notBlank(trackId);
		Validate.notBlank(userId);
		logger.info("Received request to upvote track {} for venue {} on behalf of user {}", trackId, venueId, userId);

		final DeferredResult<Vote> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.voteForTrack(venueId, trackId, userId, true).whenCompleteAsync((vote, ex) -> {
			if (ex != null) {
				logger.error("Received error when upvoting for track", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully upvoted track {} to venue {}", trackId, venueId);
				responseDeferred.setResult(vote);
			}
		});

		return responseDeferred;
	}

	@PutMapping("/vote/{venue_id}/{track_id}/{user_id}/downvote")
	public DeferredResult<Vote> downvoteTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId,
			@PathVariable("user_id") final String userId
	) {
		Validate.notBlank(venueId);
		Validate.notBlank(trackId);
		Validate.notBlank(userId);
		logger.info("Received request to downvote track {} for venue {} on behalf of user {}", trackId, venueId, userId);

		final DeferredResult<Vote> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.voteForTrack(venueId, trackId, userId, false).whenCompleteAsync((vote, ex) -> {
			if (ex != null) {
				logger.error("Received error when downvoting for track", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully downvote track {} to venue {}", trackId, venueId);
				responseDeferred.setResult(vote);
			}
		});

		return responseDeferred;
	}

	@DeleteMapping("/vote/{venue_id}/{track_id}/{user_id}")
	public DeferredResult<Void> deleteVote(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId,
			@PathVariable("user_id") final String userId
	) {
		Validate.notBlank(venueId);
		Validate.notBlank(trackId);
		Validate.notBlank(userId);
		logger.info("Received request to delete vote on track {} for venue {} on behalf of user {}", trackId, venueId, userId);

		final DeferredResult<Void> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.unvoteForTrack(venueId, trackId, userId).whenCompleteAsync((_none, ex) -> {
			if (ex != null) {
				logger.error("Received error when deleting vote for track", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully deleted voted on track {} to venue {}", trackId, venueId);
				responseDeferred.setResult(null);
			}
		});

		return responseDeferred;
	}
}
