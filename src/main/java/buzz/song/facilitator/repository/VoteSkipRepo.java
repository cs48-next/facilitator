package buzz.song.facilitator.repository;

import buzz.song.facilitator.model.VoteSkip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteSkipRepo extends JpaRepository<VoteSkip, VoteSkip.VoteSkipID> {}
