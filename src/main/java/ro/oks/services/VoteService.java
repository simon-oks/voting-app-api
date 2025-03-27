package ro.oks.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ro.oks.dtos.requests.VoteRequest;
import ro.oks.dtos.responses.CandidatResponse;
import ro.oks.dtos.responses.ResponseMessage;
import ro.oks.entities.Candidat;
import ro.oks.entities.Citoyen;
import ro.oks.entities.Election;
import ro.oks.entities.Vote;
import ro.oks.exceptions.BadRequestException;
import ro.oks.mapper.CandidatMapper;
import ro.oks.repositories.ElectionRepository;
import ro.oks.repositories.VoteRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final ElectionService electionService;
    private final ElectionRepository electionRepository;
    private final CandidatService candidatService;
    private final CitoyenService citoyenService;
    private final CandidatMapper candidatMapper;

    public ResponseMessage vote(String electionId, VoteRequest request) {
        log.info("DEBUT - Vote");
        Election election = electionService.electionById(electionId);
        Candidat candidat = candidatService.candidatById(request.getCandidatId());
        Citoyen citoyen = citoyenService.citoyenByNIE(request.getNIE());

        Optional<Vote> existVote = voteRepository.findByElectionAndCitoyen(election, citoyen);
        if (existVote.isPresent()) {
            throw new BadRequestException("This citoyen has already voted for this election");
        }

        Vote vote = Vote.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .election(election)
                .candidat(candidat)
                .citoyen(citoyen)
                .build();
        vote = voteRepository.save(vote);
        // Ajouter le vote à l'élection
        if (election.getVotes() == null) election.setVotes(new HashSet<>());
        election.getVotes().add(vote);
        electionRepository.save(election);
        log.info("FIN - Vote; terminée avec succès");
        return new ResponseMessage(HttpStatus.OK.value(), "Vote successful");
    }

    public Map<CandidatResponse, Integer> getElectionVoteStatistics(String electionId) {
        log.info("DEBUT - Récupération des statistiques d'une election dont l'ID = {}", electionId);
        Map<CandidatResponse, Integer> statistics = new HashMap<>();
        Election election = electionService.electionById(electionId);
        election.getVotes().forEach(
                        vote -> {
                            CandidatResponse candidatResponse = candidatMapper.toResponse(vote.getCandidat());
                            if (statistics.containsKey(candidatResponse)) {
                                statistics.put(candidatResponse, statistics.get(candidatResponse) + 1);
                            } else {
                                statistics.put(candidatResponse, 1);
                            }
                        }
                );
        // Classer du premier au dernier
        LinkedHashMap<CandidatResponse, Integer> collect = statistics.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Tri décroissant
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Gère les doublons (inutile ici mais requis par toMap)
                        LinkedHashMap::new // Conserve l'ordre trié
                ));
        log.info("FIN - Récupération des statistiques d'une election dont l'ID = {}; terminée avec succès", electionId);
        return collect;
    }
}
