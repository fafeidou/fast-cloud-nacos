package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 9:24
 */
public interface Lock {

    /**
     * 获取显示锁，没有获取锁的线程将要阻塞
     */
    void lock() throws InterruptedException;

    /**
     * 释放锁
     */
    void unlock();
}
