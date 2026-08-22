package com.izen.common.api.response;

import com.izen.common.api.request.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "PageResponse")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {
    @Schema(description = "데이터")
    private List<T> contents;

    @Schema(description = "데이터 갯수", example = "100")
    private long totalElements;

    @Schema(description = "전체 페이지", example = "20")
    private int totalPages;

    @Schema(description = "현재 페이지", example = "1")
    private int currentPage;

    @Schema(description = "페이지 당 데이터 사이즈", example = "10")
    private int size;

    @Schema(description = "시작 페이지", example = "1")
    private int startPage;

    @Schema(description = "끝 페이지", example = "5")
    private int endPage;

    @Schema(description = "다음 버튼 여부", example = "true")
    private boolean hasNext;

    @Schema(description = "이전 버튼 여부", example = "false")
    private boolean hasPrevious;

    public static <T> PageResponse<T> of(List<T> contents, PageRequest pageRequest, long totalElements) {
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / pageRequest.getSize());

        int startPage = ((pageRequest.getPage() - 1) / pageRequest.getBlockSize())
                * pageRequest.getBlockSize() + 1;
        int endPage = Math.min(startPage + pageRequest.getBlockSize() - 1, totalPages);

        return new PageResponse<>(
                contents, totalElements, totalPages, pageRequest.getPage(),
                pageRequest.getSize(), startPage, endPage, endPage < totalPages,
                startPage > 1
        );
    }
}
