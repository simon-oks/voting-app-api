package ro.oks.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.oks.entities.Image;
import ro.oks.exceptions.FailedToSaveImageException;
import ro.oks.exceptions.ResourceNotFoundException;
import ro.oks.repositories.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${images.dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;

    public Image upload(MultipartFile image) {
        // Créer le dossier d'upload s'il n'existe pas
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Copier le fichier sur le disque
        try {
            Files.copy(image.getInputStream(), filePath);
        } catch (IOException e) {
            log.info("Failed to save image", e);
            throw new FailedToSaveImageException("Failed to save image : " + e.getMessage());
        }

        // Enregistrer l'image
        Image profile = Image.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .fileName(fileName)
                .build();

        return imageRepository.save(profile);
    }

    public ResponseEntity<Resource> getImage(String profileId) {
        try {
            Image image = imageRepository.findById(profileId)
                    .orElseThrow(
                            () -> {
                                log.error("ERREUR: Image non trouvée");
                                return new ResourceNotFoundException("Image not found");
                            }
                    );

            String filename = image.getFileName();
            Path filePath = Paths.get(uploadDir, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
