package fast.cloud.nacos.juc.threadPool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinfuxiang
 * 创建线程工厂
 */
@FunctionalInterface
public interface ThreadPoolFactory {

    Thread createThread(Runnable runnable);

    class DefaultThreadPoolFactory implements ThreadPoolFactory {

        AtomicInteger INCREMENT = new AtomicInteger(1);
        ThreadGroup threadGroup = new ThreadGroup("DEFAULT_THREAD_GROUP");

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(threadGroup, runnable, "ThreadPool_" + INCREMENT.incrementAndGet());
        }

        DefaultThreadPoolFactory() {
        }
    }
}
