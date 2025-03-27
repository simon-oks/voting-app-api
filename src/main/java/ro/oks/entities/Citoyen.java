package ro.oks.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;
import ro.oks.dtos.requests.CitoyenRequest;

import java.time.Instant;

@Entity
@Table(name = "citoyens")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Citoyen {
    @Id
    private String id;
    @Column(unique = true)
    private String NIE;
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public void merge(Citoyen citoyen, @Valid CitoyenRequest request) {
        citoyen.setNIE(StringUtils.hasLength(request.getNIE()) ? request.getNIE() : citoyen.getNIE());
        citoyen.setName(StringUtils.hasLength(request.getName()) ? request.getName() : citoyen.getName());
    }
}
