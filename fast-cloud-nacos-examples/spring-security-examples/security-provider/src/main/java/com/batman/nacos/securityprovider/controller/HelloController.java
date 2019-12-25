package com.batman.nacos.securityprovider.controller;

import fast.cloud.nacos.securityapi.ServiceHi;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("security")
@Api(tags = {"security"}, description = "security")
public class HelloController implements ServiceHi {

    @Override
    @PreAuthorize("hasAuthority('batman')")
    public String hello() {
        return "hello world oauth2";
    }

    @Override
    @PreAuthorize("hasAuthority('batman2')")
    public String hello2() {
        return "hello world oauth2";
    }
}
