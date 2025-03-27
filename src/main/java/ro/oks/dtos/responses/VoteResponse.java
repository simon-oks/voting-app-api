package ro.oks.dtos.responses;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponse {
    private String id;
    private CitoyenResponse citoyen;
    private String createdAt;
    private String updatedAt;
}
