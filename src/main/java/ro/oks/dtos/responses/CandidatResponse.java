package ro.oks.dtos.responses;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatResponse {
    private String id;
    private String name;
    private String title;
    private String profileId;
    private String createdAt;
    private String updatedAt;
}
