package fast.cloud.nacos.apollo.example;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import java.time.LocalDateTime;

/**
 * @Classname GetConfigTest
 * @Description TODO
 * @Date 2020/3/23 14:55
 * @Created by qinfuxiang
 */
public class GetConfigTest {
    public static void main(String[] args) throws InterruptedException {
        Config config = ConfigService.getAppConfig();
        String someKey = "sms.enable";
        while (true) {
            String value = config.getProperty(someKey, null);
            System.out.printf("now: %s, sms.enable: %s%n", LocalDateTime.now().toString(),
                    value);
            Thread.sleep(3000L);
        }
    }
}
