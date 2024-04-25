package com.systemdesign.fixedwindowcounter.controller;

import com.systemdesign.fixedwindowcounter.dto.response.FixedWindowCounterProfileResponse;
import com.systemdesign.fixedwindowcounter.dto.response.FixedWindowCounterResponse;
import com.systemdesign.fixedwindowcounter.service.FixedWindowCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("fixed-window-counter")
public class FixedWindowCounterController {

    private final FixedWindowCounterService fixedWindowCounterService;

    @GetMapping
    public ResponseEntity<List<FixedWindowCounterResponse>> findAllFixedWindowCounter() {
        return ResponseEntity.status(OK)
                .body(fixedWindowCounterService.findAllFixedWindowCounter());
    }

    @PostMapping
    public ResponseEntity<FixedWindowCounterProfileResponse> createFixedWindowCounter() {
        return ResponseEntity.status(CREATED)
                .body(fixedWindowCounterService.createFixedWindowCounter());
    }
}
