package ro.oks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.oks.entities.Candidat;
import ro.oks.entities.Election;

import java.util.List;
import java.util.Set;

public interface CandidatRepository extends JpaRepository<Candidat, String> {
    Set<Candidat> findByElection(Election election);
}
