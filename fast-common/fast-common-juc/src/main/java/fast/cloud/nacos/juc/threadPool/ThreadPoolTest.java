package fast.cloud.nacos.juc.threadPool;

import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 * @Date 2020/6/23 18:59
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPool threadPool = new BasicThreadPool(2, 4, 6, 1000);

        threadPool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("Current thread is down:" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for (; ; ) {
            System.out.println(threadPool.getActiveCount());
        }
    }
}
