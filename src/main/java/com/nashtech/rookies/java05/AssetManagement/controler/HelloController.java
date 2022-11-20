package com.nashtech.rookies.java05.AssetManagement.controler;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello spring boot testing edit";
    }

     @GetMapping("/hello1")
    public String hello() {
        return "Hello spring boot testing edit 1111111";
    }
    
}
