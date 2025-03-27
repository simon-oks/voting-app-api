package ro.oks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.oks.entities.Election;

public interface ElectionRepository extends JpaRepository<Election, String> {
}
