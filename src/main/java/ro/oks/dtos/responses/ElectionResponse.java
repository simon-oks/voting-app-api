package ro.oks.dtos.responses;

import lombok.*;

import java.util.Set;


@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ElectionResponse {
    private String id;
    private String name;
    private String description;
    private Set<CandidatResponse> candidats;
    private String createdAt;
    private String updatedAt;
}
