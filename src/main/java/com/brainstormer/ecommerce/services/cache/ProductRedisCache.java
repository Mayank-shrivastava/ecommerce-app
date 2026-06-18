package com.brainstormer.ecommerce.services.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisCache {
    
    private static final String KEY_SUMMARY = "product:summary:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(1);
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<ProductResponseDto> getSummary(Long id) {
        String responseJson = stringRedisTemplate.opsForValue().get(KEY_SUMMARY + id);

        // cache miss
        if (responseJson == null) {
            log.info("Cache miss for product summary: {}", id);
            return Optional.empty();
        }

        // cache hit
        log.info("Cache hit for product summary: {}", id);
        try {
            ProductResponseDto productResponseDto = 
                    objectMapper.readValue(responseJson, ProductResponseDto.class);
            return Optional.of(productResponseDto);
        } catch (Exception e) {
            log.error("Error deserializing product summary from Redis for id {}: {}", id, e.getMessage());
            stringRedisTemplate.delete(KEY_SUMMARY + id);
            return Optional.empty();
        }
    } 

    public void putSummary(Long id, ProductResponseDto response) {
        try {
            stringRedisTemplate.opsForValue().set(
                KEY_SUMMARY + id, 
                objectMapper.writeValueAsString(response), 
                CACHE_TTL);
        } catch (Exception ex) {
            throw new RuntimeException("Error serializingg product summary to cache: " + ex.getMessage()); 
        }
    }
    
}
