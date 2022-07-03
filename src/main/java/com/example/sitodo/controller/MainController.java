package com.example.sitodo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public ResponseEntity<String> getHomePage() {
        return ResponseEntity.ok("<html><head><title>Sitodo</title></head><body></body></html>");
    }
}
