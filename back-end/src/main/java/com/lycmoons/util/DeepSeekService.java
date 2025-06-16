package com.lycmoons.util;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class DeepSeekService {

    private final WebClient webClient = WebClient.builder().baseUrl("http://127.0.0.1:8000").build();

    public CompletableFuture<String> askQuestionAsync(String question) {
        return webClient.post()
                .uri("/ask") // 调用 FastAPI 的 /ask 端点
                .bodyValue(Map.of("question", question)) // 发送 JSON 请求体
                .retrieve()
                .bodyToMono(Map.class) // FastAPI 返回的是 JSON 格式
                .map(response -> (String) response.get("answer")) // 提取 "answer" 字段
                .toFuture(); // 转换为 CompletableFuture
    }
}
