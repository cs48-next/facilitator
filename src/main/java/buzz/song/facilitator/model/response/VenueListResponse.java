package buzz.song.facilitator.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.SortedSet;

public class VenueListResponse {
	private final SortedSet<VenueListing> venues;

	@JsonCreator
	public VenueListResponse(
			final @JsonProperty("venues") SortedSet<VenueListing> venues
	) {
		this.venues = venues;
	}

	@JsonGetter("venues")
	public SortedSet<VenueListing> getVenues() {
		return venues;
	}

	public static class VenueListing implements Comparable<VenueListing> {
		private final String venueId;
		private final String venueName;
		private final double distance;

		public VenueListing(@JsonProperty("venue_id") final String venueId,
							@JsonProperty("venue_name") final String venueName,
							@JsonProperty("distance") final double distance
		) {
			this.venueId = venueId;
			this.venueName = venueName;
			this.distance = distance;
		}

		@JsonGetter("venue_id")
		public String getVenueId() {
			return venueId;
		}

		@JsonGetter("venue_name")
		public String getVenueName() {
			return venueName;
		}

		@JsonGetter("distance")
		public double getDistance() {
			return distance;
		}

		@Override
		public int compareTo(final VenueListing o) {
			return Double.compare(distance, o.distance);
		}
	}
}
