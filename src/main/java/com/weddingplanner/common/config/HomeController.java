package com.weddingplanner.common.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Smoke-test controller: http://localhost:8080/ should show the home page. */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
