package com.axa.shell.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class FrontendMappingController {

    private final FrontendCache frontendCache;

    /**
     * Serve the shell app for the root path and any non-static paths
     */
    @GetMapping(value = {
            "/",                    // Root path
            "/app*",               // Direct app routes (e.g., /app1)
            "/app*/**",            // App routes with sub-paths (e.g., /app1/dashboard)
            "/{path:[^.]*}"        // Any path without a file extension
    }, produces = "text/html")
    public String serveApp() {
        return frontendCache.getReplacedFrontendApp();
    }

    /**
     * Handle static resources
     */
    @GetMapping(value = "/static/{file}.js", produces = "application/javascript;charset=UTF-8")
    public String getJS(HttpServletResponse response, @PathVariable("file") String file) {
        response.setHeader("content-type", "application/javascript");
        return frontendCache.getFileContent("static/" + file + ".js");
    }

    @GetMapping(value = "/static/{file}.css", produces = "text/css;charset=UTF-8")
    public String getCSS(HttpServletResponse response, @PathVariable("file") String file) {
        response.setHeader("content-type", "text/css");
        return frontendCache.getFileContent("static/" + file + ".css");
    }

    @GetMapping(value = "/static/{file}.png", produces = "application/png")
    public String getPng(@PathVariable("file") String file) {
        return frontendCache.getFileContent("static/" + file + ".png");
    }

    @GetMapping(value = "/static/{file}")
    public String getAnything(@PathVariable("file") String file) {
        return frontendCache.getFileContent("static/" + file);
    }

    /**
     * Handle favicon request
     */
    @GetMapping("/favicon.ico")
    public String getFavicon() {
        return frontendCache.getFileContent("static/favicon.ico");
    }
}