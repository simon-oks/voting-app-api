package ro.oks.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.oks.dtos.responses.CandidatResponse;
import ro.oks.dtos.responses.CitoyenResponse;
import ro.oks.entities.Candidat;
import ro.oks.entities.Election;
import ro.oks.entities.Image;
import ro.oks.exceptions.ResourceNotFoundException;
import ro.oks.mapper.CandidatMapper;
import ro.oks.repositories.CandidatRepository;
import ro.oks.repositories.ElectionRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidatService {

    private final CandidatRepository candidatRepository;
    private final ImageService imageService;
    private final ElectionService electionService;
    private final ElectionRepository electionRepository;
    private final CandidatMapper candidatMapper;

    public CandidatResponse getCandidat(String id){
        return candidatMapper.toResponse(candidatById(id));
    }

    public CandidatResponse addCandidat(
            String electionId,
            String name,
            String title,
            MultipartFile profile
    ){
        log.info("DEBUT - Enregistrement d'un nouveau candidat à l'élection ID = {}", electionId);
        Election election = electionService.electionById(electionId);
        // Enregistrer le profile
        Image image = imageService.upload(profile);
        // Construire le candidat
        Candidat candidat = Candidat.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .name(name)
                .title(title)
                .profile(image)
                .election(election)
                .build();
        Candidat savedCandidat = candidatRepository.save(candidat);
        // Ajouter le cadidat à l'élection
        if (election.getCandidats() == null) election.setCandidats(new HashSet<>());
        election.getCandidats().add(savedCandidat);
        electionRepository.save(election);
        log.info("FIN - Enregistrement d'un nouveau candidat à l'élection ID = {}; terminée avec succès", electionId);
        return candidatMapper.toResponse(savedCandidat);
    }

    public CandidatResponse updateCandidat(String candidatId,
                                          String name,
                                          String title,
                                          MultipartFile profile) {
        log.info("DEBUT - Modification d'un candidat dont l'ID = {}", candidatId);
        Candidat candidat = candidatById(candidatId);
        if (profile != null) {
            Image image = imageService.upload(profile);
            candidat.setProfile(image);
        }
        candidat.setName(name);
        candidat.setTitle(title);
        Candidat savedCandidat = candidatRepository.save(candidat);
        log.info("FIN - Modification d'un candidat dont l'ID = {}; terminée avec succès", candidatId);
        return candidatMapper.toResponse(savedCandidat);
    }

    public void deleteCandidat(String electionId, String id) {
        log.info("DEBUT - Suppression d'un candidat dont l'ID = {}", id);
        Election election = electionService.electionById(electionId);
        Candidat candidat = candidatById(id);
        election.getCandidats().remove(candidat);
        candidatRepository.delete(candidat);
        log.info("FIN - Suppression d'un candidat dont l'ID = {}; terminée avec succès", id);
    }

    public Set<CandidatResponse> getCandidatsByElectionId(String electionId) {
        log.info("DEBUT - Les candidats de l'election ID = {}", electionId);
        Election election = electionService.electionById(electionId);
        Set<CandidatResponse> candidatResponses = candidatRepository.findByElection(election).stream()
                .map(candidatMapper::toResponse)
                .collect(Collectors.toSet());
        log.info("FIN - Les candidats de l'election ID = {}; terminée avec succès", electionId);
        return candidatResponses;
    }


    Candidat candidatById(String id) {
        log.info("DEBUT - Récupération d'un candidat avec l'ID = {}", id);
        Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.error("ERREUR: Candidat non trouvé");
                            return new ResourceNotFoundException("Candidat not found");
                        }
                );
        log.info("FIN - Récupération d'un candidat avec l'ID = {}; terminé avec succès", id);
        return candidat;
    }
}
