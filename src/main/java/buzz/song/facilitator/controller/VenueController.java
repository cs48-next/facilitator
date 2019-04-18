package buzz.song.facilitator.controller;

import buzz.song.facilitator.model.Venue;
import buzz.song.facilitator.model.VenueCreateRequest;
import buzz.song.facilitator.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

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
	public DeferredResult<Venue> createVenue(@RequestBody final VenueCreateRequest createRequest) {
		logger.info("Received request to create venue at lat: {}, long: {}", createRequest.getLatitude(), createRequest.getLongitude());

		final DeferredResult<Venue> responseDeferred = new DeferredResult<>(controllerTimeout);
		responseDeferred.onTimeout(() -> responseDeferred.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT) .body("Request timeout occurred.")));

		venueService.createVenue(createRequest.getLatitude(), createRequest.getLongitude()).whenCompleteAsync((venue, ex) -> {
			if (ex != null) {
				logger.error("Received request when creating venue", ex);
				responseDeferred.setErrorResult(ex);
			} else {
				logger.info("Successfully created venue {}", venue.getId());
				responseDeferred.setResult(venue);
			}
		});

		return responseDeferred;
	}
}
