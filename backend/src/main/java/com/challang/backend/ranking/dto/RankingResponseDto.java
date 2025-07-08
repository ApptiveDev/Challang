package com.challang.backend.ranking.dto;

import com.challang.backend.liquor.entity.Liquor;
import lombok.Getter;

@Getter
public class RankingResponseDto {
    private final Long liquorId;
    private final String name;
    private final Long archiveCount;

    public RankingResponseDto(Liquor liquor, Long archiveCount, String s3BaseUrl) {
        this.liquorId = liquor.getId();
        this.name = liquor.getName();
        this.archiveCount = archiveCount;
    }
}