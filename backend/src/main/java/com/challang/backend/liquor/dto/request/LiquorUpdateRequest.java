package com.challang.backend.liquor.dto.request;

import com.challang.backend.tag.dto.request.LiquorTagRequest;
import jakarta.validation.Valid;
import java.util.List;

public record LiquorUpdateRequest(

        String name,
        String imageUrl,
        String tastingNote,
        String origin,
        Double minAbv,
        Double maxAbv,
        Long levelId,
        Long typeId,
        List<@Valid LiquorTagRequest> liquorTags
) {
}
