package ro.oks.web;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.oks.services.ImageService;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageApi {

    private final ImageService imageService;

    @GetMapping("/{profileId}")
    public ResponseEntity<Resource> getImage(
            @Parameter(name = "profileId", description = "ID du profile")
            @PathVariable String profileId){
        return imageService.getImage(profileId);
    }
}
