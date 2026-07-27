package com.izen.common.api.response;

import com.izen.common.api.request.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "PageResponse")
public record PageResponse<T>(
        @Schema(description = "데이터")
        List<T> contents,

        @Schema(description = "데이터 갯수", example = "100")
        long totalElements,

        @Schema(description = "전체 페이지", example = "20")
        int totalPages,

        @Schema(description = "현재 페이지", example = "1")
        int currentPage,

        @Schema(description = "페이지 당 데이터 사이즈", example = "10")
        int size,

        @Schema(description = "시작 페이지", example = "1")
        int startPage,

        @Schema(description = "끝 페이지", example = "5")
        int endPage,

        @Schema(description = "다음 버튼 여부", example = "true")
        boolean hasNext,

        @Schema(description = "이전 버튼 여부", example = "false")
        boolean hasPrevious
) {
    public static <T> PageResponse<T> of(List<T> contents, PageRequest pageRequest, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageRequest.getSize());

        int startPage = ((pageRequest.getPage() - 1) / pageRequest.getBlockSize())
                * pageRequest.getBlockSize() + 1;
        int endPage = startPage + pageRequest.getBlockSize() - 1;

        return new PageResponse<>(
                contents, totalElements, totalPages, pageRequest.getPage(),
                pageRequest.getSize(), startPage, endPage, endPage < totalPages,
                startPage > 1
        );
    }
}
