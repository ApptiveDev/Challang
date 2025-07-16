package com.challang.backend.liquor.dto.request;

import com.challang.backend.tag.dto.request.LiquorTagRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record LiquorCreateRequest(

        @NotBlank(message = "술 이름은 필수입니다.")
        String name,

        @NotBlank(message = "술 이미지는 필수입니다.")
        String imageUrl,

        @NotBlank(message = "테이스팅 노트는 필수입니다. 술의 주요 향미나 특징을 입력해주세요.")
        String tastingNote,

        @NotBlank(message = "유래는 필수입니다. 간략한 술의 역사나 기원을 입력해주세요.")
        String origin,

        @NotNull(message = "최소 도수는 필수입니다.")
        Double minAbv,

        @NotNull(message = "최대 도수는 필수입니다.")
        Double maxAbv,

        @NotNull(message = "도수 등급 ID는 필수입니다.")
        Long levelId,

        @NotNull(message = "주종 ID는 필수입니다.")
        Long typeId,

        @NotNull(message = "태그 목록은 필수입니다.")
        @Size(min = 1, message = "최소 한 개 이상의 태그를 선택해주세요.")
        List<@Valid LiquorTagRequest> liquorTags
) {
}
