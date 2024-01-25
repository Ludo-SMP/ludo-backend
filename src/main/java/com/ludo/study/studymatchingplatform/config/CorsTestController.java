package com.ludo.study.studymatchingplatform.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsTestController {

    @GetMapping("/test")
    public String getData() {
        return "data";
    }
}
