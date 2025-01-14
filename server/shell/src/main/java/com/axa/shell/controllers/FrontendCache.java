package com.axa.shell.controllers;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class FrontendCache {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String getReplacedFrontendApp() {
        return cache.computeIfAbsent("/static/index.html", key -> {
            try {
                String content = StreamUtils.copyToString(new ClassPathResource(key).getInputStream(), StandardCharsets.UTF_8);
                log.info("Successfully loaded index.html");
                return content;
            } catch (IOException e) {
                log.error("Failed to load index.html from {}", key, e);
                return null;
            }
        });
    }

    @Nullable
    public String getFileContent(@NonNull String fileName) {
        return cache.computeIfAbsent(fileName, key -> {
            try {
                String content = StreamUtils.copyToString(new ClassPathResource(key).getInputStream(), StandardCharsets.UTF_8);
                log.info("Successfully loaded file: {}", key);
                return content;
            } catch (IOException e) {
                log.error("Failed to load file from {}", key, e);
                return null;
            }
        });
    }
}
