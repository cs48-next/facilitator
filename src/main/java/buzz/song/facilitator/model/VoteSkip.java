package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a vote to skip the current {@link Track} on a {@link Venue} playlist.
 */
@Entity
@Table(name = "vote_skip")
@IdClass(VoteSkip.VoteSkipID.class)
public class VoteSkip implements Comparable<VoteSkip>{
	@Id
	private String venueId;

	@Id
	private String userId;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Timestamp createdOn;

	@UpdateTimestamp
	@Column(name = "modified_on", updatable = false)
	private Timestamp modifiedOn;

	@JsonCreator
	public VoteSkip(
			@JsonProperty("venue_id") final String venueId,
			@JsonProperty("user_id") final String userId
	) {
		this.venueId = venueId;
		this.userId = userId;
	}

	private VoteSkip() {}

	@JsonGetter("venue_id")
	public String getVenueId() {
		return venueId;
	}

	@JsonGetter("user_id")
	public String getUserId() {
		return userId;
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
		return "VoteSkip{" +
				"venueId='" + venueId + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VoteSkip voteSkip = (VoteSkip) o;
		return Objects.equals(venueId, voteSkip.venueId) &&
				Objects.equals(userId, voteSkip.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(venueId, userId);
	}

	@Override
	public int compareTo(VoteSkip o) {
		return 0;
	}

	public static class VoteSkipID implements Serializable {
		private String venueId;
		private String userId;

		public VoteSkipID(final String venueId, final String userId) {
			this.venueId = venueId;
			this.userId = userId;
		}

		private VoteSkipID() {}

		@Override
		public String toString() {
			return "VoteSkipID{" +
					"venueId='" + venueId + '\'' +
					", userId='" + userId + '\'' +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			final VoteSkipID voteID = (VoteSkipID) o;
			return Objects.equals(userId, voteID.userId) &&
					Objects.equals(venueId, voteID.venueId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(userId, venueId);
		}
	}
}
