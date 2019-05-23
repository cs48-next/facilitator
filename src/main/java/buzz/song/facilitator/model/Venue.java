package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

/**
 * Represents a user-created venue.
 */
@Entity
@Table(name = "venue")
public class Venue {

	@Id
	private String id;

	private String name;

	private String hostName;
	private String hostId;

	private String currentTrackId;

	private double timeProgress;
	private double totalTime;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "venueId", referencedColumnName = "id", updatable = false)
	private Set<VoteSkip> voteSkips;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "venueId", referencedColumnName = "id", updatable = false)
	@SortNatural
	private SortedSet<Track> playlist;

	private double latitude;
	private double longitude;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "venueId", updatable = false)
	private VenueStats venueStats;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Timestamp createdOn;

	@UpdateTimestamp
	@Column(name = "modified_on", updatable = false)
	private Timestamp modifiedOn;

	private Timestamp closedOn;

	@JsonCreator
	public Venue(
			@NotNull @JsonProperty("id") final String id,
			@NotNull @JsonProperty("name") final String name,
			@NotNull @JsonProperty("host_name") final String hostName,
			@NotNull @JsonProperty("host_id") final String hostId,
			@NotNull @JsonProperty("vote_skips") final Set<VoteSkip> voteSkips,
			@NotNull @JsonProperty("current_track_id") final String currentTrackId,
			@NotNull @JsonProperty("playlist") final SortedSet<Track> playlist,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude,
			@JsonProperty("stats") final VenueStats venueStats
	) {
		this.id = id;
		this.name = name;
		this.hostName = hostName;
		this.hostId = hostId;
		this.voteSkips = voteSkips;
		this.currentTrackId = currentTrackId;
		this.playlist = playlist;
		this.latitude = latitude;
		this.longitude = longitude;
		this.venueStats = venueStats;
	}

	private Venue() {}

	@JsonGetter("id")
	public String getId() {
		return id;
	}

	@JsonGetter("name")
	public String getName() {
		return name;
	}

	@JsonGetter("host_name")
	public String getHostName() {
		return hostName;
	}

	@JsonGetter("host_id")
	public String getHostId() {
		return hostId;
	}

	@JsonGetter("stats")
	public VenueStats getVenueStats() {
		return venueStats;
	}

	public void setVenueStats(VenueStats venueStats) {
		this.venueStats = venueStats;
	}

	@JsonGetter("current_track_id")
	public String getCurrentTrackId() {
		return currentTrackId;
	}

	public void setCurrentTrackId(final String currentTrackId) {
		this.currentTrackId = currentTrackId;
	}

	@JsonGetter("vote_skips")
	public Set<VoteSkip> getVoteSkips() {
		return voteSkips;
	}

	@JsonGetter("playlist")
	public SortedSet<Track> getPlaylist() {
		return playlist;
	}

	@JsonGetter("latitude")
	public double getLatitude() {
		return latitude;
	}

	@JsonGetter("longitude")
	public double getLongitude() {
		return longitude;
	}

	@JsonGetter("time_progress")
	public double getTimeProgress() {
		return timeProgress;
	}

	public void setTimeProgress(double timeProgress) {
		this.timeProgress = timeProgress;
	}

	@JsonGetter("total_time")
	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	@JsonGetter("created_on")
	public Timestamp getCreatedOn() {
		return createdOn;
	}

	@JsonGetter("modified_on")
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	@JsonGetter("closed_on")
	public Timestamp getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(final Timestamp closedOn) {
		this.closedOn = closedOn;
	}

	@Override
	public String toString() {
		return "Venue{" + "id='" + id + '\'' +
				", name='" + name + '\'' +
				", hostName='" + hostName + '\'' +
				", hostId='" + hostId + '\'' +
				", voteSkips='" + voteSkips + '\'' +
				", currentTrackId='" + currentTrackId + '\'' +
				", playlist=" + playlist +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Venue venue = (Venue) o;
		return Objects.equals(id, venue.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
