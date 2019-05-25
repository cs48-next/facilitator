package buzz.song.facilitator.repository;

import buzz.song.facilitator.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VenueRepository extends JpaRepository<Venue, String> {
	@Transactional
	@Modifying
	@Query("UPDATE Venue SET time_progress=(:time_progress), total_time=(:total_time) WHERE id=(:id)")
	void updateTimeProgress(@Param("id") final String venueId, @Param("time_progress") final double cur, @Param("total_time") final double total);
}
