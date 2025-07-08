package com.challang.backend.ranking.service;

import com.challang.backend.archive.repository.ArchiveRepository;
import com.challang.backend.liquor.entity.Liquor;
import com.challang.backend.liquor.repository.LiquorRepository;
import com.challang.backend.ranking.dto.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final ArchiveRepository archiveRepository;
    private final LiquorRepository liquorRepository;

    public List<RankingResponseDto> getTop8ArchivedLiquors() {
        List<Object[]> results = archiveRepository.findTop8ArchivedLiquors(PageRequest.of(0, 8));

        return results.stream()
                .map(result -> {
                    Long liquorId = (Long) result[0];
                    Long archiveCount = (Long) result[1];
                    Liquor liquor = liquorRepository.findById(liquorId).orElse(null);
                    if (liquor != null) {
                        return new RankingResponseDto(liquor, archiveCount);
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}