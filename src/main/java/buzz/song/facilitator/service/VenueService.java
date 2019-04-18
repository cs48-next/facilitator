package buzz.song.facilitator.service;

import buzz.song.facilitator.model.Venue;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class VenueService {

	/**
	 * Creates a {@link Venue} at the given coordinates
	 *
	 * @param latitude  latitutde of GPS location
	 * @param longitude longtitude of GPS location
	 * @return Future for created Venue
	 */
	public CompletableFuture<Venue> createVenue(final double latitude, final double longitude) {
		return CompletableFuture.completedFuture(new Venue(UUID.randomUUID().toString(), latitude, longitude));
	}
}
