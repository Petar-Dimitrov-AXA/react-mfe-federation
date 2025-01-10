package com.axa.app2.controllers;

import jakarta.annotation.Nullable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FrontendCache {
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String getReplacedFrontendApp() {
        return cache.computeIfAbsent("/static/index.html", key -> {
            try {
                return StreamUtils.copyToString(new ClassPathResource(key).getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return null;
            }
        });
    }

    @Nullable
    public String getFileContent(@NonNull String fileName) {
        return cache.computeIfAbsent(fileName, key -> {
            try {
                return StreamUtils.copyToString(new ClassPathResource(fileName).getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return null;
            }
        });
    }
}
