package fast.cloud.nacos.juc.threadPool;

/**
 * @author qinfuxiang
 */
public interface RunnableQueue {

    /**
     * 当任务到coreSize时，会放到队列上去
     */
    void offer(Runnable runnable);

    /**
     * 获取任务
     */
    Runnable take();

    /**
     * 获取任务数
     */
    int size();

}
