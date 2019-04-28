package buzz.song.facilitator.repository;

import buzz.song.facilitator.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TrackRepository extends JpaRepository<Track, Track.TrackID> {}
