package buzz.song.facilitator.repository;

import buzz.song.facilitator.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VenueRepository extends JpaRepository<Venue, String> {}
