package buzz.song.facilitator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "venue_stats")
public class VenueStats {
    @Id
    private String venueId;
    private int totalTracksPlayed;
    private int totalTracksSkipped;
    private int totalTracksProposed;
    private int totalTrackUpvotes;
    private int totalTrackDownvotes;

    @JsonCreator
    public VenueStats(
            @NonNull @JsonProperty("venue_id") final String venueId,
            @JsonProperty("total_tracks_played") final int totalTracksPlayed,
            @JsonProperty("total_tracks_skipped") final int totalTracksSkipped,
            @JsonProperty("total_tracks_proposed") final int totalTracksProposed,
            @JsonProperty("total_track_upvotes") final int totalTrackUpvotes,
            @JsonProperty("total_track_downvotes") final int totalTrackDownvotes
    ) {
        this.venueId = venueId;
        this.totalTracksPlayed = totalTracksPlayed;
        this.totalTracksSkipped = totalTracksSkipped;
        this.totalTracksProposed = totalTracksProposed;
        this.totalTrackUpvotes = totalTrackUpvotes;
        this.totalTrackDownvotes = totalTrackDownvotes;
    }

    private VenueStats() {}

    @JsonGetter("venue_id")
    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    @JsonGetter("total_tracks_played")
    public int getTotalTracksPlayed() {
        return totalTracksPlayed;
    }

    public void setTotalTracksPlayed(int totalTracksPlayed) {
        this.totalTracksPlayed = totalTracksPlayed;
    }

    @JsonGetter("total_tracks_skipped")
    public int getTotalTracksSkipped() {
        return totalTracksSkipped;
    }

    public void setTotalTracksSkipped(int totalTracksSkipped) {
        this.totalTracksSkipped = totalTracksSkipped;
    }

    @JsonGetter("total_tracks_proposed")
    public int getTotalTracksProposed() {
        return totalTracksProposed;
    }

    public void setTotalTracksProposed(int totalTracksProposed) {
        this.totalTracksProposed = totalTracksProposed;
    }

    @JsonGetter("total_track_upvotes")
    public int getTotalTrackUpvotes() {
        return totalTrackUpvotes;
    }

    public void setTotalTrackUpvotes(int totalTrackUpvotes) {
        this.totalTrackUpvotes = totalTrackUpvotes;
    }

    @JsonGetter("total_track_downvotes")
    public int getTotalTrackDownvotes() {
        return totalTrackDownvotes;
    }

    public void setTotalTrackDownvotes(int totalTrackDownvotes) {
        this.totalTrackDownvotes = totalTrackDownvotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VenueStats that = (VenueStats) o;
        return venueId.equals(that.venueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueId);
    }
}
