package ro.oks.dtos.requests;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequest {
    private String candidatId;
    private String NIE; // Numéro d'identification de l'électeur
}
