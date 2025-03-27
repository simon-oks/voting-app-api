package ro.oks.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitoyenRequest {
    @NotNull(message = "NIE can't be null")
    @NotNull(message = "NIE can't be empty")
    private String NIE;
    private String name;
}
