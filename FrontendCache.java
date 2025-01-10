package com.axa.ch.amhub.common.controller.frontend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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

    @Value("${stage}")
    private String stage;

    @Value("${app.backend.base.api}")
    private String baseApi;

    @Value("${app.meta-data.api.esg}")
    private String metaApi;

    @Value("${app.backend.base.path}")
    private String basePath;

    @Value("${app.backend.base.version}")
    private String baseVersion;

    @Value("${app.login.url}")
    private String loginUrl;

    @Value("${app.logout.url}")
    private String logoutUrl;

    @Value("${app.login.autorefresh}")
    private String autorefresh;

    @Value("${app.links}")
    private String links;

    public String getReplacedFrontendApp() {
        return cache.computeIfAbsent("/static/index.html", key -> {
            try {
                String html = StreamUtils.copyToString(new ClassPathResource(key).getInputStream(), StandardCharsets.UTF_8);
                return html.replace("@{am-hub.app.backend.api}", baseApi + basePath + baseVersion)
                        .replace("@{am-hub.app.meta-data.api.esg}", metaApi)
                        .replace("@{am-hub.app.login}", loginUrl)
                        .replace("@{am-hub.app.logout}", logoutUrl)
                        .replace("@{am-hub.app.autorefresh}", autorefresh)
                        .replace("@{am-hub.app.stage}", stage)
                        .replace("@{am-hub.app.links}", links);
            } catch (IOException e) {
                log.warn("Failed to read file {}: {}", key, e.getMessage());
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
                log.warn("Failed to read file {}: {}", fileName, e.getMessage());
                return null;
            }
        });
    }
}
