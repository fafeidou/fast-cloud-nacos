package fast.cloud.nacos.juc.threadPool;

import java.util.LinkedList;

/**
 * @author qinfuxiang
 */
public class LinkedRunnableQueue implements RunnableQueue {

    private final int limit;

    private final DenyPolicy denyPolicy;

    private final LinkedList<Runnable> runnableList = new LinkedList<>();

    private final ThreadPool threadPool;

    public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }

    @Override
    public void offer(Runnable runnable) {
        synchronized (runnableList) {
            if (runnableList.size() >= limit) {
                //无法容纳新任务时，执行拒绝策略
                denyPolicy.reject(runnable, threadPool);
            } else {
                //将任务加入队尾，并且唤醒阻塞中的线程
                runnableList.add(runnable);
                runnableList.notifyAll();
            }
        }
    }

    @Override
    public Runnable take() {
        synchronized (runnableList) {
            while (runnableList.isEmpty()) {
                try {
                    //如果
                    runnableList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return runnableList.removeFirst();
    }

    @Override
    public int size() {
        synchronized (runnableList) {
            return runnableList.size();
        }
    }
}
