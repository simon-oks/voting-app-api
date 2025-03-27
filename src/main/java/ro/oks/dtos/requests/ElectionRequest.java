package ro.oks.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ElectionRequest {
    @NotNull(message = "Name can't be null")
    @NotNull(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Start date can't be null")
    @NotNull(message = "Start date can't be empty")
    private String startDate;
    @NotNull(message = "End date can't be null")
    @NotNull(message = "End date can't be empty")
    private String endDate;
    private String description;
}
