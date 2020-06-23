package fast.cloud.nacos.juc.threadPool;

/**
 * @author qinfuxiang
 */
public interface ThreadPool {

    /**
     * 提交任务到线程池
     */
    void execute(Runnable runnable);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 获取初始化线程数
     */
    int getInitSize();

    /**
     * 获取核心线程数
     */
    int getCoreSize();

    /**
     * 获取最大线程数
     */
    int getMaxSize();

    /**
     * 获取阻塞队列最大长度
     */
    int getQueueSize();

    /**
     * 获取线程池中活跃线程的数量
     */
    int getActiveCount();

    /**
     * 查看线程池是否已经shutdown
     */
    boolean isShutDown();
}
