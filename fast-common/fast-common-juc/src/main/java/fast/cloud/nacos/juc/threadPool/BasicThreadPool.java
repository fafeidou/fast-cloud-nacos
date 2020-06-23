package fast.cloud.nacos.juc.threadPool;

import fast.cloud.nacos.juc.threadPool.DenyPolicy.DiscardDenyPolicy;
import fast.cloud.nacos.juc.threadPool.ThreadPoolFactory.DefaultThreadPoolFactory;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 */
public class BasicThreadPool extends Thread implements ThreadPool {

    private final int initSize;

    private final int coreSize;

    private final int maxSize;

    private int activeCount;

    private final ThreadPoolFactory threadPoolFactory;

    private final RunnableQueue runnableQueue;

    private volatile boolean isShutdown = false;

    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();

    private final static ThreadPoolFactory DEFAULT_THREAD_POOL_FACTORY = new DefaultThreadPoolFactory();

    private final static DenyPolicy DEFAULT_DENY_POLICY = new DiscardDenyPolicy();

    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    public BasicThreadPool(int initSize, int coreSize, int maxSize, int queueSize) {
        this(initSize, coreSize, maxSize, DEFAULT_THREAD_POOL_FACTORY, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS,
            queueSize);
    }

    public BasicThreadPool(int initSize, int coreSize, int maxSize, ThreadPoolFactory threadPoolFactory,
        DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit, int queueSize) {
        this.initSize = initSize;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.threadPoolFactory = threadPoolFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    private void init() {
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    private void newThread() {
        InternalTask internalTask = new InternalTask(runnableQueue);
        Thread thread = this.threadPoolFactory.createThread(internalTask);
        ThreadTask threadTask = new ThreadTask(internalTask, thread);
        threadQueue.offer(threadTask);
        this.activeCount++;
    }

    @Override
    public void execute(Runnable runnable) {
        if (this.isShutdown) {
            throw new IllegalStateException("the thread pool is down.");
        }
        this.runnableQueue.offer(runnable);
    }

    @Override
    public void run() {
        while (!this.isShutdown && !this.isInterrupted()) {
            try {
                timeUnit.sleep(keepAliveTime);
            } catch (InterruptedException e) {
                isShutdown = true;
                break;
            }
            //如果当前队列有尚未执行的任务，并且小于coreSize，将队列扩展到coreSize
            if (runnableQueue.size() > 0 && activeCount < coreSize) {
                for (int i = initSize; i < coreSize; i++) {
                    newThread();
                }
                continue;//不能一下扩容到maxSize
            }

            //如果当前队列有尚未执行的任务，并且小于maxSize，将队列扩展到maxSize
            if (runnableQueue.size() > 0 && activeCount < maxSize) {
                for (int i = coreSize; i < maxSize; i++) {
                    newThread();
                }
            }

            //队列没有任务，
            if (runnableQueue.size() == 0 && activeCount > maxSize) {
                for (int i = maxSize; i < activeCount; i++) {
                    removeThread();
                }
            }

        }
    }

    @Override
    public void shutdown() {
        synchronized (this) {
            if (isShutdown) {
                return;
            }

            threadQueue.forEach(threadTask -> {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });

            this.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        return this.initSize;
    }

    @Override
    public int getCoreSize() {
        return this.coreSize;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public int getQueueSize() {
        return this.runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        synchronized (this) {
            return this.activeCount;
        }
    }

    @Override
    public boolean isShutDown() {
        return false;
    }

    private void removeThread() {
        ThreadTask threadTask = threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }

    public static class ThreadTask {

        InternalTask internalTask;
        Thread thread;

        ThreadTask(InternalTask internalTask, Thread thread) {
            this.thread = thread;
            this.internalTask = internalTask;
        }
    }
}
