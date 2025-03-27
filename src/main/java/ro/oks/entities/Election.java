package ro.oks.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;
import ro.oks.dtos.requests.ElectionRequest;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "elections")
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Election {
    @Id
    private String id;
    private String name;
    private Instant startDate;
    private Instant endDate;
    private String description;

    @OneToMany(mappedBy = "election", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Candidat> candidats = new HashSet<>();

    @OneToMany(mappedBy = "election", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
