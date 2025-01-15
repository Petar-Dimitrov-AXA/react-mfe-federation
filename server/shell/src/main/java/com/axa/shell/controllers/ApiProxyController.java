package com.axa.shell.controllers;

import com.axa.shell.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * API Proxy Controller for Microfrontend Architecture
 * <p>
 * This controller acts as a reverse proxy for API requests in a microfrontend setup where
 * multiple frontend applications are served through a shell application. It ensures that
 * API requests are forwarded to their respective backend services.
 * <p>
 * Use Case:
 * When accessing micro frontends through the shell app (e.g., localhost:3000/app1),
 * API requests need to be forwarded to their respective backend services:
 * - /app1/api/* → localhost:3001/app1/api/*
 * - /app2/api/* → localhost:3002/app2/api/*
 * - /app3/api/* → localhost:3003/app3/api/*
 */
@RestController
@RequestMapping("/")  // Changed to root path
@RequiredArgsConstructor
public class ApiProxyController {

    private final ProxyService proxyService;  // Made final to work with @RequiredArgsConstructor

    /**
     * Handles all API requests and proxies them to their respective backend services.
     * Supports GET, POST, PUT, and DELETE HTTP methods.
     *
     * @param request The original HTTP request containing the URL, method, headers, and body
     * @return ResponseEntity<String> The response from the proxied backend service
     * @throws IOException If there's an error reading the request body
     */
    @RequestMapping(value = "/*/api/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequest(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return proxyService.proxyRequest(
                request.getRequestURI(),
                HttpMethod.valueOf(request.getMethod()),
                new HttpHeaders(),
                body
        );
    }
}