package ro.oks.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.oks.dtos.requests.ElectionRequest;
import ro.oks.dtos.responses.ElectionResponse;
import ro.oks.entities.Election;
import ro.oks.exceptions.ResourceNotFoundException;
import ro.oks.mapper.ElectionMapper;
import ro.oks.repositories.ElectionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final ElectionMapper electionMapper;

    public Set<ElectionResponse> getElections(){
        log.info("DEBUT - Récupération de tous les elections");
        Set<ElectionResponse> electionResponses = electionRepository.findAll().stream()
                .sorted(Comparator.comparing(Election::getCreatedAt))
                .map(electionMapper::toResponse)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // Pour conserver l'ordre trié dans un Set

        log.info("FIN - Récupération de tous les elections; terminée avec succès");
        return electionResponses;
    }

    public ElectionResponse getElectionById(String id){
        return electionMapper.toResponse(electionById(id));
    }

    public ElectionResponse addElection(ElectionRequest request){
        log.info("DEBUT - Enregistrement d'une nouvelle election");
        Election election = electionMapper.toEntity(request);
        // Affecter l'ID
        election.setId(UUID.randomUUID().toString().replace("-", ""));
        election = electionRepository.save(election);
        ElectionResponse electionResponse = electionMapper.toResponse(election);
        log.info("FIN - Enregistrement d'une nouvelle election; terminée avec succès");
        return electionResponse;
    }

    public ElectionResponse updateElection(String id, ElectionRequest request){
        log.info("DEBUT - Mise à jour d'une election dont l'ID = {}", id);
        Election election = electionById(id);
        electionMapper.merge(election, request);
        ElectionResponse electionResponse = electionMapper.toResponse(
                electionRepository.save(election)
        );
        log.info("DEBUT - Mise à jour d'une election dont l'ID = {}; terminée avec succès", id);
        return electionResponse;
    }

    public void deleteElection(String id){
        log.info("DEBUT - Suppression d'une election dont l'ID = {}", id);
        electionRepository.delete(electionById(id));
        log.info("FIN - Suppression d'une election dont l'ID = {}; terminée avec succès", id);
    }

    Election electionById(String id){
        log.info("DEBUT - Récupération d'une election avec l'ID = {}", id);
        Election election = electionRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.error("ERREUR: Election non trouvée");
                            return new ResourceNotFoundException("Election not found");
                        }
                );
        log.info("FIN - Récupération d'une election avec l'ID = {}; terminée avec succès", id);
        return election;
    }
}
