package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

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

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumns({
			@JoinColumn(name = "venueId", referencedColumnName = "venueId", updatable = false),
			@JoinColumn(name = "trackId", referencedColumnName = "trackId", updatable = false)
	})
	private Set<Vote> votes;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Timestamp createdOn;

	@UpdateTimestamp
	@Column(name = "modified_on", updatable = false)
	private Timestamp modifiedOn;

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

	@JsonGetter("created_on")
	public Timestamp getCreatedOn() {
		return createdOn;
	}

	@JsonGetter("modified_on")
	public Timestamp getModifiedOn() {
		return modifiedOn;
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
		final Comparator<Track> comparator = Comparator
				.comparing((final Track track) -> track.votes.stream().mapToInt(v -> v.isUpvote() ? 1 : -1).sum())
				.thenComparing(Track::getTrackId)
				.thenComparing(Track::getVenueId);

		return comparator.compare(o, this);
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
