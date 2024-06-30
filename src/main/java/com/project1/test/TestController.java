package com.project1.test;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")

public class TestController {
    @PostMapping("/ali")
    public String ali()
    {
        return "ali";
    }

}
