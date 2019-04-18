package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user request to create a {@link Venue}.
 */
public class VenueCreateRequest {
	private final double latitude;
	private final double longitude;

	@JsonCreator
	public VenueCreateRequest(
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.latitude = latitude;
		this.longitude = longitude;
	}


	@JsonGetter
	public double getLatitude() {
		return latitude;
	}

	@JsonGetter
	public double getLongitude() {
		return longitude;
	}
}
