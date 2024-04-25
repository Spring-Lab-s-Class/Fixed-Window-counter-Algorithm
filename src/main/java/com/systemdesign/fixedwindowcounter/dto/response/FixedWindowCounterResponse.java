package com.systemdesign.fixedwindowcounter.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FixedWindowCounterResponse {

    private String key;
    private Long requestCount;

    public static FixedWindowCounterResponse from(String key, Long requestCount) {
        return FixedWindowCounterResponse.builder()
                .key(key)
                .requestCount(requestCount)
                .build();
    }
}
