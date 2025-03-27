package ro.oks.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.oks.dtos.requests.CitoyenRequest;
import ro.oks.dtos.responses.CitoyenResponse;
import ro.oks.entities.Citoyen;
import ro.oks.exceptions.ResourceNotFoundException;
import ro.oks.mapper.CitoyenMapper;
import ro.oks.repositories.CitoyenRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitoyenService {

    private final CitoyenRepository citoyenRepository;
    private final CitoyenMapper citoyenMapper;

    public Set<CitoyenResponse> getCitoyens() {
        log.info("DEBUT - Récupération de tous les citoyens");
        Set<CitoyenResponse> citoyenResponses = citoyenRepository.findAll().stream()
                .map(citoyenMapper::toResponse)
                .collect(Collectors.toSet());
        log.info("FIN - Récupération de tous les citoyens; terminée avec succès");
        return citoyenResponses;
    }

    public CitoyenResponse getCitoyenById(String id) {
        return citoyenMapper.toResponse(citoyenById(id));
    }

    public CitoyenResponse getCitoyenByNIE(String NIE) {
        return citoyenMapper.toResponse(citoyenByNIE(NIE));
    }

    public CitoyenResponse addCitoyen(@Valid CitoyenRequest request) {
        log.info("DEBUT - Enregistrement d'un nouveau citoyen");
        Citoyen citoyen = citoyenMapper.toEntity(request);
        // Affecter l'ID
        citoyen.setId(UUID.randomUUID().toString().replace("-", ""));
        citoyen = citoyenRepository.save(citoyen);
        CitoyenResponse citoyenResponse = citoyenMapper.toResponse(citoyen);
        log.info("FIN - Enregistrement d'un nouveau citoyen; terminée avec succès");
        return citoyenResponse;
    }

    public CitoyenResponse updateCitoyen(String id, @Valid CitoyenRequest request) {
        log.info("DEBUT - Mise à jour du citoyen dont l'ID = {}", id);
        Citoyen citoyen = citoyenById(id);
        citoyen.merge(citoyen, request);
        CitoyenResponse savedCitoyen = citoyenMapper.toResponse(
                citoyenRepository.save(citoyen)
        );
        log.info("DEBUT - Mise à jour du citoyen dont l'ID = {}; terminée avec succès", id);
        return savedCitoyen;
    }

    private Citoyen citoyenById(String id){
        log.info("DEBUT - Récupération d'un citoyen avec l'ID = {}", id);
        Citoyen citoyen = citoyenRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.error("ERREUR: Citoyen non trouvé");
                            return new ResourceNotFoundException("Citoyen not found");
                        }
                );
        log.info("FIN - Récupération d'un citoyen avec l'ID = {}; terminé avec succès", id);
        return citoyen;
    }

    Citoyen citoyenByNIE(String nie) {
        log.info("DEBUT - Récupération d'un citoyen avec le Numéro d'Identification Electeur = {}", nie);
        Citoyen citoyen = citoyenRepository.findByNIE(nie)
                .orElseThrow(
                        () -> {
                            log.error("ERREUR: Citoyen non trouvé ");
                            return new ResourceNotFoundException("Citoyen not found");
                        }
                );
        log.info("FIN - Récupération d'un citoyen avec le Numéro d'Identification Electeur = {}", nie);
        return citoyen;
    }
}
