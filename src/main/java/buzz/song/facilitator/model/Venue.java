package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.SortNatural;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "venueId", referencedColumnName = "id")
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
			@NotNull @JsonProperty("playlist") final SortedSet<Track> playlist,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.id = id;
		this.name = name;
		this.hostName = hostName;
		this.hostId = hostId;
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
				", playlist=" + playlist +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
