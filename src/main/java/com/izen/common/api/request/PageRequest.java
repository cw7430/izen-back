package com.izen.common.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "PageRequest")
public class PageRequest {
    @Min(value = 1, message = "1 이상만 가능합니다.")
    private Integer page = 1;

    @Min(value = 1, message = "1 이상 100 이하만 가능합니다.")
    @Max(value = 100, message = "1 이상 100 이하만 가능합니다.")
    private Integer size = 5;

    @Min(value = 5, message = "5 이상 10 이하만 가능합니다.")
    @Max(value = 10, message = "5 이상 10 이하만 가능합니다.")
    private Integer blockSize = 5;
}
