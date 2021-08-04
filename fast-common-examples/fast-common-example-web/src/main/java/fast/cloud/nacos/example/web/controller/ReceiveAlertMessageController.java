package fast.cloud.nacos.example.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
@RequestMapping("alertMessage")
@Slf4j
public class ReceiveAlertMessageController {

    @PostMapping("receive")
    public String receiveMsg(@RequestBody byte[] data) {
        String msg = new String(data, 0, data.length, Charset.forName("UTF-8"));
        log.info("接收AlertManager预警消息：" + msg);
        return "success";
    }
}

