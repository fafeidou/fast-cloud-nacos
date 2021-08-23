package fast.cloud.nacos.servicehi;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceHiApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory factory = (new ThreadFactoryBuilder())
                .setNameFormat("PollingServerListUpdater-%d")
                .setDaemon(true)
                .build();
        ScheduledThreadPoolExecutor _serverListRefreshExecutor = new ScheduledThreadPoolExecutor(2, factory);
        ScheduledFuture<?> scheduledFuture = _serverListRefreshExecutor.scheduleWithFixedDelay(
                () -> System.out.println("123123123123"),
                1000,
                30 * 1000,
                TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(50000);
    }

}
