package buzz.song.facilitator.model.request;

import buzz.song.facilitator.model.Venue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user request to create a {@link Venue}.
 */
public class VenueCreateRequest {
	private final String name;
	private final double latitude;
	private final double longitude;

	@JsonCreator
	public VenueCreateRequest(
			@JsonProperty("name") final String name,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@JsonGetter("name")
	public String getName() {
		return name;
	}

	@JsonGetter("latitude")
	public double getLatitude() {
		return latitude;
	}

	@JsonGetter("longitude")
	public double getLongitude() {
		return longitude;
	}
}
