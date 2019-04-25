package buzz.song.facilitator.repository;

import buzz.song.facilitator.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Vote.VoteID> {}
