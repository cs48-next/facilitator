package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a vote for a {@link Track} on a {@link Venue} playlist.
 */
@Entity
@Table(name = "vote")
@IdClass(Vote.VoteID.class)
public class Vote {
	@Id
	private String venueId;
	@Id
	private String trackId;
	@Id
	private String userId;
	private boolean upvote;

	@JsonCreator
	public Vote(
			@JsonProperty("venue_id") final String venueId,
			@JsonProperty("track_id") final String trackId,
			@JsonProperty("user_id") final String userId,
			@JsonProperty("upvote") final boolean upvote
	) {
		this.venueId = venueId;
		this.trackId = trackId;
		this.userId = userId;
		this.upvote = upvote;
	}

	private Vote() {}

	@JsonGetter("venue_id")
	public String getVenueId() {
		return venueId;
	}

	@JsonGetter("track_id")
	public String getTrackId() {
		return trackId;
	}

	@JsonGetter("user_id")
	public String getUserId() {
		return userId;
	}

	@JsonGetter("upvote")
	public boolean isUpvote() {
		return upvote;
	}

	@Override
	public String toString() {
		return "Vote{" + "venueId='" + venueId + '\'' +
				", trackId='" + trackId + '\'' +
				", userId='" + userId + '\'' +
				", upvote=" + upvote +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Vote vote = (Vote) o;
		return upvote == vote.upvote &&
				Objects.equals(trackId, vote.trackId) &&
				Objects.equals(userId, vote.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(trackId, userId, upvote);
	}

	public static class VoteID implements Serializable {
		private String venueId;
		private String trackId;
		private String userId;

		public VoteID(final String venueId, final String trackId, final String userId) {
			this.venueId = venueId;
			this.trackId = trackId;
			this.userId = userId;
		}

		private VoteID() {}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			final VoteID voteID = (VoteID) o;
			return Objects.equals(trackId, voteID.trackId) &&
					Objects.equals(userId, voteID.userId) &&
					Objects.equals(venueId, voteID.venueId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(trackId, userId, venueId);
		}
	}
}
