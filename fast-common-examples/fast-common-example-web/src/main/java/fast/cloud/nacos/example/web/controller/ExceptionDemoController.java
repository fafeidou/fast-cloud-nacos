package fast.cloud.nacos.example.web.controller;

import fast.cloud.nacos.common.model.exception.BadRequestException;
import fast.cloud.nacos.common.model.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exception")
public class ExceptionDemoController {

    @GetMapping("demoA")
    public ApiResponse<String> demoA() {
        throw new BadRequestException("报错了。。。。");
    }

    @GetMapping("demoB")
    public ApiResponse<String> demoB() {
        throw new BadRequestException("报错了。。。。");
    }
}
