package ro.oks.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.oks.dtos.requests.CandidatRequest;
import ro.oks.dtos.requests.ElectionRequest;
import ro.oks.dtos.requests.VoteRequest;
import ro.oks.dtos.responses.CandidatResponse;
import ro.oks.dtos.responses.CitoyenResponse;
import ro.oks.dtos.responses.ElectionResponse;
import ro.oks.dtos.responses.ResponseMessage;
import ro.oks.services.CandidatService;
import ro.oks.services.ElectionService;
import ro.oks.services.VoteService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/elections")
@RequiredArgsConstructor
public class ElectionApi {
    private final ElectionService electionService;
    private final CandidatService candidatService;
    private final VoteService voteService;

    @GetMapping
    @Operation(summary = "Récupérer la liste de toutes les elections enregistré")
    public ResponseEntity<Set<ElectionResponse>> getElections() {
        return ResponseEntity.ok(electionService.getElections());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une election par son ID")
    public ResponseEntity<ElectionResponse> getElectionById(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id) {
        return ResponseEntity.ok(electionService.getElectionById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle election")
    public ResponseEntity<ElectionResponse> addElection(@RequestBody @Valid ElectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(electionService.addElection(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une election")
    public ResponseEntity<ElectionResponse> updateElection(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id,
            @RequestBody @Valid ElectionRequest request) {
        return ResponseEntity.ok(electionService.updateElection(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une election")
    public ResponseEntity<Void> deleteElection(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id) {
        electionService.deleteElection(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/candidats")
    @Operation(summary = "Récupérer la liste de tous les candidats d'une election")
    public ResponseEntity<Set<CandidatResponse>> getCandidatsByElectionId(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id) {
        return ResponseEntity.ok(candidatService.getCandidatsByElectionId(id));
    }

    @PostMapping("/{id}/candidats")
    @Operation(summary = "Ajouter un candidat")
    public ResponseEntity<CandidatResponse> addCandidat(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id,
            String name,
            String title,
            MultipartFile profile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                candidatService.addCandidat(id, name, title, profile));
    }

    @PutMapping("/{id}/candidats/{candidatId}")
    @Operation(summary = "Mettre à jour un candidat")
    public ResponseEntity<CandidatResponse> updateCandidat(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id,
            @Parameter(name = "candidatId", description = "ID du candidat")
            @PathVariable String candidatId,
            String name,
            String title,
            MultipartFile profile) {
        return ResponseEntity.ok(candidatService.updateCandidat(candidatId, name, title, profile));
    }

    @DeleteMapping("/{id}/candidats/{candidatId}")
    @Operation(summary = "Supprimer un candidat")
    public ResponseEntity<Void> deleteCandidat(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id,
            @Parameter(name = "candidatId", description = "ID du candidat")
            @PathVariable String candidatId) {
        candidatService.deleteCandidat(id, candidatId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/votes")
    @Operation(summary = "Ajouter un vote")
    public ResponseEntity<ResponseMessage> addVote(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id,
            @RequestBody @Valid VoteRequest request) {
        return ResponseEntity.ok(voteService.vote(id, request));
    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "Statistique de votes")
    public ResponseEntity<Map<CandidatResponse, Integer>>  getStatistics(
            @Parameter(name = "id", description = "ID de l'election")
            @PathVariable String id){
        return ResponseEntity.ok(voteService.getElectionVoteStatistics(id));
    }
}
