package com.nashtech.rookies.java05.AssetManagement.controler;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "test deployment";
    }
}
