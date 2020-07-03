package fast.cloud.nacos.juc.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 * @Date 2020/7/3 9:09
 */
public class ThreadPoolInheritable {

    public static void main(String[] args) throws InterruptedException {
        final InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
        inheritableThreadLocal.set("aaa");
        //输出 aaa
        System.out.println(inheritableThreadLocal.get());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("========");
                System.out.println(inheritableThreadLocal.get());
                inheritableThreadLocal.set("bbb");
                System.out.println(inheritableThreadLocal.get());
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        executorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("========");
        System.out.println(inheritableThreadLocal.get());
    }

}
