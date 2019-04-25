package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Represents a track in a {@link Venue} playlist.
 */
@Entity
@Table(name = "track")
@IdClass(Track.TrackID.class)
public class Track implements Comparable<Track> {
	@Id
	private String venueId;
	@Id
	private String trackId;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "venueId", referencedColumnName = "venueId"),
			@JoinColumn(name = "trackId", referencedColumnName = "trackId")
	})
	private Set<Vote> votes;

	@JsonCreator
	public Track(
			@NotNull @JsonProperty("venue_id") final String venueId,
			@NotNull @JsonProperty("track_id") final String trackId,
			@JsonProperty("votes") final Set<Vote> votes
	) {
		this.venueId = venueId;
		this.trackId = trackId;
		this.votes = votes;
	}

	public Track() {}

	@JsonGetter("venue_id")
	public String getVenueId() {
		return venueId;
	}

	@JsonGetter("track_id")
	public String getTrackId() {
		return trackId;
	}

	@JsonGetter("votes")
	public Set<Vote> getVotes() {
		return votes;
	}

	@Override
	public String toString() {
		return "Track{" + "venueId='" + venueId + '\'' +
				", trackId='" + trackId + '\'' +
				", votes=" + votes +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Track track = (Track) o;
		return Objects.equals(trackId, track.trackId) &&
				Objects.equals(venueId, track.venueId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(trackId, venueId, votes);
	}

	@Override
	public int compareTo(final Track o) {
		final Stream<Vote> thisVotes = votes.stream();
		final Stream<Vote> thatVotes = o.votes.stream();
		return Integer.compare(
				thatVotes.mapToInt(v -> v.isUpvote() ? 1 : -1).sum(),
				thisVotes.mapToInt(v -> v.isUpvote() ? 1 : -1).sum()
		);
	}

	public static class TrackID implements Serializable {
		private String venueId;
		private String trackId;

		public TrackID(final String venueId, final String trackId) {
			this.trackId = trackId;
			this.venueId = venueId;
		}

		private TrackID() {}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			final TrackID trackID = (TrackID) o;
			return Objects.equals(trackId, trackID.trackId) &&
					Objects.equals(venueId, trackID.venueId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(trackId, venueId);
		}
	}
}
