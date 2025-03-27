package ro.oks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.oks.entities.Candidat;
import ro.oks.entities.Citoyen;
import ro.oks.entities.Election;
import ro.oks.entities.Vote;

import java.util.Optional;
import java.util.Set;

public interface VoteRepository extends JpaRepository<Vote, String> {
    Optional<Vote> findByElectionAndCitoyen(Election election, Citoyen citoyen);
    Set<Vote> findByElection(Election election);
    Set<Vote> findByCandidat(Candidat candidat);
}
