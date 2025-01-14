package com.axa.app3.controllers;

import com.axa.app3.controllers.FrontendCache;
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

    public static final String ROOT = "/";

    /**
     * Inject system properties into the index.html that bootstraps the react-client.
     *
     * @return String html page
     */
    @GetMapping(produces = "text/html")
    public String getRootPage() {
        return frontendCache.getReplacedFrontendApp();
    }

    @GetMapping(value = ROOT, produces = "text/html")
    public String getRootPageToo() {
        return frontendCache.getReplacedFrontendApp();
    }

    /**
     * Serve Javascript
     *
     * @return String getJS
     */
    @GetMapping(value = "/static/{file}.js", produces = "application/javascript;charset=UTF-8")
    public String getJS(HttpServletResponse response, @PathVariable("file") String file) {
        response.setHeader("content-type", "application/javascript");
        return frontendCache.getFileContent("static/" + file + ".js");
    }

    /**
     * Serve CSS
     *
     * @return String getCSS
     */
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

    @GetMapping(value = "/{path:^(?!swagger-ui).*}", produces = "text/html")
    public String getEverythingElseExceptSwaggerUi() {
        return frontendCache.getReplacedFrontendApp();
    }

    @GetMapping(value = "/{path:^(?!swagger-ui).*}/**", produces = "text/html")
    public String getEverythingElseInclSubpathsExceptSwaggerUi() {
        return frontendCache.getReplacedFrontendApp();
    }
}
