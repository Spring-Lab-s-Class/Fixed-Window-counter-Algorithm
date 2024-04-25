package com.systemdesign.fixedwindowcounter.service;

import com.systemdesign.fixedwindowcounter.dto.response.FixedWindowCounterResponse;
import com.systemdesign.fixedwindowcounter.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.systemdesign.fixedwindowcounter.exception.RateExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixedWindowCounterService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final static String FIXED_WINDOW_KEY = "FixedWindow:"; // 키
    private final static long FIXED_WINDOW_MAX_REQUEST = 1000; // 최대 요청 허용 수
    private final static long FIXED_WINDOW_DURATION = 60; // 60초


    public FixedWindowCounterResponse createFixedWindowCounter() {
        String redisKey = generateKey();
        Long currentCount = redisTemplate.opsForValue().increment(redisKey, 1);

        if (currentCount == 1) {
            log.info("Fixed Window Counter created. key: {}", redisKey);
            redisTemplate.expire(redisKey, FIXED_WINDOW_DURATION, TimeUnit.SECONDS);
        }

        if (currentCount > FIXED_WINDOW_MAX_REQUEST) {
            log.error("Rate limit exceeded. key: {}", redisKey);
            throw new RateLimitExceededException(COMMON_TOO_MANY_REQUESTS);
        }

        return FixedWindowCounterResponse.from(redisKey, currentCount);
    }

    public List<FixedWindowCounterResponse> findAllFixedWindowCounter() {
        Set<String> keys = redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(FIXED_WINDOW_KEY + "*").count(1000).build())) {
                Set<String> keySet = new HashSet<>();
                while (cursor.hasNext()) {
                    keySet.add(new String(cursor.next()));
                }
                return keySet;
            }
        });

        List<FixedWindowCounterResponse> responses = new ArrayList<>();
        for (String key : keys) {
            Object rawValue = redisTemplate.opsForValue().get(key);
            log.info("rawValue: {}", rawValue);

            if (rawValue instanceof Long requestCount) {
                responses.add(FixedWindowCounterResponse.from(key, requestCount));
                log.info("responses: {}", responses.size());
            }
        }
        return responses;
    }

    private String generateKey() {
        long windowStartTimestamp = System.currentTimeMillis() / (FIXED_WINDOW_DURATION * 1000) * (FIXED_WINDOW_DURATION * 1000);
        log.info("windowStartTimestamp: {}", windowStartTimestamp);
        return FIXED_WINDOW_KEY + windowStartTimestamp;
    }
}
