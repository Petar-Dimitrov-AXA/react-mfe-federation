package com.axa.shell.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProxyService {
    private static final Map<String, String> APP_ROUTES = Map.of(
            "app1", "http://localhost:3001",
            "app2", "http://localhost:3002",
            "app3", "http://localhost:3003"
    );


    private final RestTemplate restTemplate;

    public ResponseEntity<String> proxyRequest(String path, HttpMethod method, HttpHeaders headers, String body) {
        for (Map.Entry<String, String> entry : APP_ROUTES.entrySet()) {
            String appPrefix = "/" + entry.getKey() + "/api";
            if (path.startsWith(appPrefix)) {
                String targetUrl = entry.getValue() + path;
                HttpEntity<String> entity = new HttpEntity<>(body, headers);
                return restTemplate.exchange(targetUrl, method, entity, String.class);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
