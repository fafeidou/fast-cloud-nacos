package fast.cloud.nacos.juc.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 * @Date 2020/7/3 9:21
 */
public class ThreadPoolTransmittable {

    public static void main(String[] args) throws InterruptedException {
        final TransmittableThreadLocal transmittableThreadLocal = new TransmittableThreadLocal();
        transmittableThreadLocal.set("aaa");
        //输出 aaa
        System.out.println(transmittableThreadLocal.get());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("========");
                System.out.println(transmittableThreadLocal.get());
                transmittableThreadLocal.set("bbb");
                System.out.println(transmittableThreadLocal.get());
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(executorService);
        ttlExecutorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        ttlExecutorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("========");
        System.out.println(transmittableThreadLocal.get());
    }
}
