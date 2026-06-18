package com.brainstormer.ecommerce.services.cache;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.brainstormer.ecommerce.dtos.ProductResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRedisCache {
    
    private static final String KEY_SUMMARY = "product:summary:";
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<ProductResponseDto> getSummary(Long id) {
        String responseJson = stringRedisTemplate.opsForValue().get(KEY_SUMMARY + id);

        // cache miss
        if (responseJson == null) {
            return Optional.empty();
        }

        // cache hit
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

    private void putSummary(Long id, ProductResponseDto response) {
        try {
            stringRedisTemplate.opsForValue().set(KEY_SUMMARY + id, objectMapper.writeValueAsString(response));
        } catch (Exception ex) {
            throw new RuntimeException("Error serializingg product summary to cache: " + ex.getMessage()); 
        }
    }
    
}
