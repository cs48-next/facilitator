package buzz.song.facilitator.controller;

import buzz.song.facilitator.model.Track;
import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.Vote;
import buzz.song.facilitator.model.request.VenueCreateRequest;
import buzz.song.facilitator.model.response.VenueListResponse;
import buzz.song.facilitator.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entry point for API requests related to venues.
 */
@Controller
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
	@ResponseBody
	public DeferredResult<Venue> createVenue(
			@RequestBody final VenueCreateRequest createRequest
	) {
		final String name = createRequest.getName();
		final double latitude = createRequest.getLatitude();
		final double longitude = createRequest.getLongitude();

		logger.info("Received request to create venue {} at lat: {}, long: {}", name, latitude, longitude);

		final DeferredResult<Venue> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.createVenue(name, latitude, longitude).whenCompleteAsync((venue, ex) -> {
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
	@ResponseBody
	public DeferredResult<VenueListResponse> listVenues(
			@RequestParam("latitude") final double latitude,
			@RequestParam("longitude") final double longitude
	) {
		logger.info("Received request to list venues near lat: {}, long: {}", latitude, longitude);

		final DeferredResult<VenueListResponse> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.listVenues(latitude, longitude).whenCompleteAsync((venues, ex) -> {
			if (ex != null) {
				logger.error("Received error when listing venues", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				final VenueListResponse listing = new VenueListResponse(
						venues.entrySet().stream()
								.map(entry -> new VenueListResponse.VenueListing(
										entry.getKey().getId(), entry.getKey().getName(), entry.getValue()
								)).collect(Collectors.toCollection(TreeSet::new))
				);
				logger.info("Found venues {}", venues);
				responseDeferred.setResult(listing);
			}
		});

		return responseDeferred;
	}

	@GetMapping("/venue/{venue_id}")
	@ResponseBody
	public DeferredResult<Venue> fetchVenue(
			@PathVariable("venue_id") final String venueId
	) {
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

	@PostMapping("/track/{venue_id}/{track_id}")
	@ResponseBody
	public DeferredResult<Track> proposeTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId
	) {
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

	@PutMapping("/vote/{venue_id}/{track_id}/upvote")
	@ResponseBody
	public DeferredResult<Vote> upvoteTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId
	) {
		logger.info("Received request to upvote track {} for venue {}", trackId, venueId);

		final DeferredResult<Vote> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.voteForTrack(venueId, trackId, UUID.randomUUID().toString(), true).whenCompleteAsync((vote, ex) -> {
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

	@PutMapping("/vote/{venue_id}/{track_id}/downvote")
	@ResponseBody
	public DeferredResult<Vote> downvoteTrack(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId
	) {
		logger.info("Received request to downvote track {} for venue {}", trackId, venueId);

		final DeferredResult<Vote> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.voteForTrack(venueId, trackId, UUID.randomUUID().toString(), false).whenCompleteAsync((vote, ex) -> {
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

	@DeleteMapping("/vote/{venue_id}/{track_id}}")
	@ResponseBody
	public DeferredResult<Void> deleteVote(
			@PathVariable("venue_id") final String venueId,
			@PathVariable("track_id") final String trackId
	) {
		logger.info("Received request to delete vote on track {} for venue {}", trackId, venueId);

		final DeferredResult<Void> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

		venueService.unvoteForTrack(venueId, trackId, UUID.randomUUID().toString()).whenCompleteAsync((_none, ex) -> {
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
