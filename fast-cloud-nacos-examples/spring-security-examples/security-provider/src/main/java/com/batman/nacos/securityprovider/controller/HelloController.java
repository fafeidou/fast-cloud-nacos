package com.batman.nacos.securityprovider.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("security")
@Api(tags = {"security"}, description = "security")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "hello world oauth2";
    }
}
