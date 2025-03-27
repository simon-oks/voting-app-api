package ro.oks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.oks.entities.Citoyen;

import java.util.Optional;

public interface CitoyenRepository extends JpaRepository<Citoyen, String> {
    Optional<Citoyen> findByNIE(String NIE);
}
