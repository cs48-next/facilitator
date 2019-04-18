package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Represents a user-created venue.
 */
public class Venue {
	private final String id;
	private final double latitude;
	private final double longitude;

	@JsonCreator
	public Venue(
			@NotNull @JsonProperty("id") final String id,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@JsonGetter
	public String getId() {
		return id;
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
