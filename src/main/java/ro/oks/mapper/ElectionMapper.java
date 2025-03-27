package ro.oks.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import ro.oks.dtos.requests.ElectionRequest;
import ro.oks.dtos.responses.ElectionResponse;
import ro.oks.entities.Election;
import ro.oks.utils.DateConverter;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElectionMapper {

    private final CandidatMapper candidatMapper;

    public ElectionResponse toResponse(Election election) {
        if (election == null) return null;
        return ElectionResponse.builder()
                .id(election.getId())
                .name(election.getName())
                .description(election.getDescription())
                .candidats(election.getCandidats().stream().map(candidatMapper::toResponse).collect(Collectors.toSet()))
                .createdAt(election.getCreatedAt().toString())
                .updatedAt(election.getUpdatedAt().toString())
                .build();
    }

    public Election toEntity(ElectionRequest request) {
        if (request == null) return null;
        return Election.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(DateConverter.convertDateToInstant(request.getStartDate()))
                .endDate(DateConverter.convertDateToInstant(request.getEndDate()))
                .build();
    }

    public void merge(Election election, ElectionRequest request) {
        election.setName(StringUtils.hasLength(request.getName()) ? request.getName() : election.getName());
        election.setStartDate(DateConverter.convertDateToInstant(request.getStartDate()));
        election.setEndDate(DateConverter.convertDateToInstant(request.getEndDate()));
        election.setDescription(StringUtils.hasLength(request.getDescription()) ? request.getDescription() : election.getDescription());
    }
}
