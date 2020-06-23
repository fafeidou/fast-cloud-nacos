package fast.cloud.nacos.juc.threadPool;

import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 * @Date 2020/6/23 18:59
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPool threadPool = new BasicThreadPool(2, 4, 6, 1000);
        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("Current thread is down:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (; ; ) {
            System.out.println("getActiveCount-----" + threadPool.getActiveCount());
            System.out.println("getInitSize-----" + threadPool.getInitSize());
            System.out.println("getCoreSize-----" + threadPool.getCoreSize());
            System.out.println("getMaxSize-----" + threadPool.getMaxSize());
            System.out.println("getQueueSize-----" + threadPool.getQueueSize());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
