package com.izen.common.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "PageRequest")
public class PageRequest {
    Integer page = 1;
    Integer size = 5;
    Integer blockSize = 5;
}
