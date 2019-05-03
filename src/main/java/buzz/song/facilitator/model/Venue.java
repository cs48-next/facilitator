package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "venueId", referencedColumnName = "id", updatable = false)
	@SortNatural
	private SortedSet<Track> playlist;
	private double latitude;
	private double longitude;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Timestamp createdOn;

	@UpdateTimestamp
	@Column(name = "modified_on", updatable = false)
	private Timestamp modifiedOn;

	@JsonCreator
	public Venue(
			@NotNull @JsonProperty("id") final String id,
			@NotNull @JsonProperty("name") final String name,
			@NotNull @JsonProperty("host_name") final String hostName,
			@NotNull @JsonProperty("host_id") final String hostId,
			@NotNull @JsonProperty("current_track_id") final String currentTrackId,
			@NotNull @JsonProperty("playlist") final SortedSet<Track> playlist,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.id = id;
		this.name = name;
		this.hostName = hostName;
		this.hostId = hostId;
		this.currentTrackId = currentTrackId;
		this.playlist = playlist;
		this.latitude = latitude;
		this.longitude = longitude;
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

	@JsonGetter("current_track_id")
	public String getCurrentTrackId() {
		return currentTrackId;
	}

	public void setCurrentTrackId(final String currentTrackId) {
		this.currentTrackId = currentTrackId;
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
		return "Venue{" + "id='" + id + '\'' +
				", name='" + name + '\'' +
				", hostName='" + hostName + '\'' +
				", hostId='" + hostId + '\'' +
				", currentTrackId='" + currentTrackId + '\'' +
				", playlist=" + playlist +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
