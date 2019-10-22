package fast.cloud.nacos.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("message")
public class MessageDemoController {
    @Autowired
    private MessageSource messageSource;

    /**
     * ## http://localhost:8888/message/getMessageByKey?lang=zh_CN
     * ## http://localhost:8888/message/getMessageByKey?lang=en_US
     * @return
     */
    @RequestMapping("getMessageByKey")
    public String getMessageByKey() {
        return getMessage("hello");
    }

    public String getMessage(String messageCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, args, locale);
    }

}
