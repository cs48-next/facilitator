package buzz.song.facilitator.model.request;

import buzz.song.facilitator.model.Venue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

/**
 * Represents a user request to create a {@link Venue}.
 */
public class VenueCreateRequest {
	private final String venueName;
	private final String hostName;
	private final String hostId;
	private final double latitude;
	private final double longitude;

	@JsonCreator
	public VenueCreateRequest(
			@JsonProperty("venue_name") final String venueName,
			@JsonProperty("host_name") final String hostName,
			@JsonProperty("host_id") final String hostId,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.venueName = Validate.notBlank(venueName);
		this.hostName = Validate.notBlank(hostName);
		this.hostId = Validate.notBlank(hostId);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@JsonGetter("venue_name")
	public String getVenueName() {
		return venueName;
	}

	@JsonGetter("host_name")
	public String getHostName() {
		return hostName;
	}

	@JsonGetter("host_id")
	public String getHostId() {
		return hostId;
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
