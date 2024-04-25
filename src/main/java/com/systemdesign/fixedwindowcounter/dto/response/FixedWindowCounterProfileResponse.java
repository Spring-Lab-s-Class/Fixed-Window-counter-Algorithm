package com.systemdesign.fixedwindowcounter.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FixedWindowCounterProfileResponse {

    private List<FixedWindowCounterResponse> counters;

    public static FixedWindowCounterProfileResponse from(List<FixedWindowCounterResponse> counters) {
        return FixedWindowCounterProfileResponse.builder()
                .counters(counters)
                .build();
    }
}