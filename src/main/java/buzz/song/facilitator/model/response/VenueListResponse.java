package buzz.song.facilitator.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
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
		private final String hostName;
		private final String hostId;
		private final String currentTrackId;
		private final boolean closed;
		private final double distance;
		private final long ageMs;

		public VenueListing(@JsonProperty("venue_id") final String venueId,
							@JsonProperty("venue_name") final String venueName,
							@JsonProperty("host_name") final String hostName,
							@JsonProperty("host_id") final String hostId,
							@JsonProperty("current_track_id") final String currentTrackId,
							@JsonProperty("closed") final boolean closed,
							@JsonProperty("distance") final double distance,
							@JsonProperty("age_ms") final long ageMs
		) {
			this.venueId = venueId;
			this.venueName = venueName;
			this.hostName = hostName;
			this.hostId = hostId;
			this.currentTrackId = currentTrackId;
			this.closed = closed;
			this.distance = distance;
			this.ageMs = ageMs;
		}

		@JsonGetter("venue_id")
		public String getVenueId() {
			return venueId;
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

		@JsonGetter("current_track_id")
		public String getCurrentTrackId() {
			return currentTrackId;
		}

		@JsonGetter("closed")
		public boolean isClosed() {
			return closed;
		}

		@JsonGetter("distance")
		public double getDistance() {
			return distance;
		}

		@JsonGetter("age_ms")
		public long getAgeMs() {
			return ageMs;
		}

		@Override
		public int compareTo(final VenueListing o) {

			final Comparator<VenueListing> comparator = Comparator
					.comparing(VenueListing::getDistance)
					.thenComparing(VenueListing::getAgeMs)
					.thenComparing(VenueListing::getVenueId);

			return comparator.compare(this, o);
		}
	}
}
