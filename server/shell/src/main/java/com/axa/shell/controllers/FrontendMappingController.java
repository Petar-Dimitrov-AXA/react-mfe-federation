package com.axa.shell.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${spring.application.name}")
@RequiredArgsConstructor
public class FrontendMappingController {

    private final FrontendCache frontendCache;

    /**
     * Serve static assets (JS, CSS, images)
     */
    @GetMapping(value = "/static/{file}/**")
    public String getStaticFiles(@PathVariable("file") String file, HttpServletResponse response) {
        String filePath = "static/" + file;

        // Set content type based on file extension
        if (file.endsWith(".js")) {
            response.setHeader("content-type", "application/javascript");
        } else if (file.endsWith(".css")) {
            response.setHeader("content-type", "text/css");
        } else if (file.endsWith(".png")) {
            response.setHeader("content-type", "image/png");
        }

        return frontendCache.getFileContent(filePath);
    }

    /**
     * Forward all other requests to the frontend app
     * This needs to be the last mapping
     */
    @RequestMapping(value = "/**", produces = "text/html")
    public String forward() {
        return frontendCache.getReplacedFrontendApp();
    }
}
