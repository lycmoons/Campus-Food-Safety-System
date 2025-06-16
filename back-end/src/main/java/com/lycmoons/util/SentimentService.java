package com.lycmoons.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class SentimentService {

    private final WebClient webClient = WebClient.builder().baseUrl("http://127.0.0.1:8000").build();

    public CompletableFuture<Float> getSentimentScoreAsync(String comment) {
        return webClient.post()
                .uri("/predict")
                .bodyValue(Map.of("text", comment))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> ((Number) response.get("score")).floatValue())
                .toFuture(); // 返回一个异步的 CompletableFuture
    }
}
