package ro.oks.dtos.responses;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitoyenResponse {
    private String id;
    private String NIE;
    private String name;
    private String createdAt;
    private String updatedAt;
}
