package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.SortComparator;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
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
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "venueId", referencedColumnName = "id")
	@SortNatural
	private SortedSet<Track> playlist;
	private double latitude;
	private double longitude;

	@JsonCreator
	public Venue(
			@NotNull @JsonProperty("id") final String id,
			@NotNull @JsonProperty("name") final String name,
			@NotNull @JsonProperty("playlist") final SortedSet<Track> playlist,
			@JsonProperty("latitude") final double latitude,
			@JsonProperty("longitude") final double longitude
	) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "Venue{" + "id='" + id + '\'' +
				", name='" + name + '\'' +
				", playlist=" + playlist +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
