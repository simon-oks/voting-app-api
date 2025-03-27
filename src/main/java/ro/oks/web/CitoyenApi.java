package ro.oks.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.oks.dtos.requests.CitoyenRequest;
import ro.oks.dtos.responses.CitoyenResponse;
import ro.oks.services.CitoyenService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/citoyens")
@RequiredArgsConstructor
public class CitoyenApi {

    private final CitoyenService citoyenService;

    @GetMapping
    @Operation(summary = "Récupérer la liste de tous les citoyens enregistré")
    public ResponseEntity<Set<CitoyenResponse>> getCitoyens(){
        return ResponseEntity.ok(citoyenService.getCitoyens());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un citoyen par son ID")
    public ResponseEntity<CitoyenResponse> getCitoyenById(
            @Parameter(name = "id", description = "ID du citoyen")
            @PathVariable String id) {
        return ResponseEntity.ok(citoyenService.getCitoyenById(id));
    }

    @GetMapping("/nie/{nie}")
    @Operation(summary = "Récupérer un citoyen par son Numéro d'Identification Electeur")
    public ResponseEntity<CitoyenResponse> getCitoyenByNie(
            @Parameter(name = "nie", description = "Numéro d'Identification Electeur")
            @PathVariable String nie) {
        return ResponseEntity.ok(citoyenService.getCitoyenByNIE(nie));
    }

    @PostMapping
    @Operation(summary = "Enregistrer un nouveau citoyen")
    public ResponseEntity<CitoyenResponse> addCitoyen(@RequestBody @Valid CitoyenRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(citoyenService.addCitoyen(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un citoyen existant")
    public ResponseEntity<CitoyenResponse> addCitoyen(
            @Parameter(name = "id", description = "ID du citoyen")
            @PathVariable String id,
            @RequestBody @Valid CitoyenRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(citoyenService.updateCitoyen(id, request));
    }

}
