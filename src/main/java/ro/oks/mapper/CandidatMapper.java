package ro.oks.mapper;

import org.springframework.stereotype.Component;
import ro.oks.dtos.responses.CandidatResponse;
import ro.oks.entities.Candidat;

@Component
public class CandidatMapper {
    public CandidatResponse toResponse(Candidat candidat) {
        if (candidat == null) return null;
        return CandidatResponse.builder()
                .id(candidat.getId())
                .name(candidat.getName())
                .title(candidat.getTitle())
                .profileId(candidat.getProfile().getId())
                .createdAt(candidat.getCreatedAt().toString())
                .updatedAt(candidat.getUpdatedAt().toString())
                .build();
    }
}
