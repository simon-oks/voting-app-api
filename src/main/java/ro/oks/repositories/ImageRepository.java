package ro.oks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.oks.entities.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

}
