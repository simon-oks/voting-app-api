package ro.oks.mapper;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import ro.oks.dtos.requests.CitoyenRequest;
import ro.oks.dtos.responses.CitoyenResponse;
import ro.oks.entities.Citoyen;

@Component
public class CitoyenMapper {

    public CitoyenResponse toResponse(Citoyen citoyen) {
        if (citoyen == null) return null;
        return CitoyenResponse.builder()
                .id(citoyen.getId())
                .NIE(citoyen.getNIE())
                .name(citoyen.getName())
                .createdAt(citoyen.getCreatedAt().toString())
                .updatedAt(citoyen.getUpdatedAt().toString())
                .build();
    }

    public Citoyen toEntity(@Valid CitoyenRequest request) {
        if (request == null) return null;
        return Citoyen.builder()
                .NIE(request.getNIE())
                .name(request.getName())
                .build();
    }
}
