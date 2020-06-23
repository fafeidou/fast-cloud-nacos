package fast.cloud.nacos.juc.threadPool;

/**
 * @author qinfuxiang
 */
public class InternalTask implements Runnable {

    private final RunnableQueue runnableQueue;

    private volatile boolean running = true;

    InternalTask(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        //当前任务状态为running并且没有被中断，则其不断从queue中获取runnable，然后执行run方法
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable runnable = runnableQueue.take();
                runnable.run();
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
