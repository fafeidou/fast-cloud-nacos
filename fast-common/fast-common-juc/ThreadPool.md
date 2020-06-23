线程池原理及自定义线程池
@[toc]
# 线程池原理

所谓线程池，通俗的讲就是有一个池子，里面存放着已经创建好的线程，当有任务提交到池子中时，池子中的某个线程会自动执行任务，如果池子中的线程数量不够，
则会创建更多的线程来完成任务，但是该数量是有限的。但是该数量是有限的。当任务较少时，如果池子中的线程数量不够能够自动回收。为了能够缓存没有被完成的
任务和异步提交任务，需要一个任务队列。

通过上面描述可知，一个完整的线程池需要以下参数：

* 任务队列：用于缓存提交的任务。
* 线程数量管理功能，有三个参数，最大线程数maxSize，初始化线程数initSize，核心线程数coreSize,它们之间的关系是initSize<=coreSize<=maxSize。
* 拒绝策略：当线程数量已经达到上限且任务队列已满，这个时候要有一定的策略来通知任务提交者。
* QueueSize：主要是用來存放提交的Runnable，但是要有一个limit限制，防止内存溢出。
* KeepAlive：线程参数的自动维护时间，也就是说达到maxSize的时候，间隔多长时间恢复到coreSize。

# 线程池接口定义

## ThreadPool

```java
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
```

## RunnableQueue

该queue是一个BlockingQueue，并且有limit限制

```java
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

```

## ThreadPoolFactory
个性化定制thread，比如Thread应该放到那个ThreadGroup中，优先级、线程名字、是否守护线程。

```java

@FunctionalInterface
public interface ThreadPoolFactory {

    Thread createThread(Runnable runnable);
}
```
## DenyPolicy
当RunnableQueue的数量达到limit上限时，应该有拒绝策略通知任务提交者。
```java
@FunctionalInterface
public interface DenyPolicy {

    void reject(Runnable runnable, ThreadPool threadPool);

    class DiscardDenyPolicy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            // do nothing
        }
    }

    class AbortDenyPolicy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new  RuntimeException("The runnable:" + runnable +"will be abort.");
        }
    }

    class RunnerDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if (!threadPool.isShutDown()) {
                runnable.run();
            }
        }
    }
}
```
* DiscardDenyPolicy，任何事情都不做，丢弃掉

* AbortDenyPolicy，抛出异常

* RunnerDenyPolicy，使用调用者线程执行

##  InternalTask

该类实现Runnable接口，不断从RunnableQueue上取出数据

```java

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
```

# 线程池自定义详细实现

## LinkedRunnableQueue

```java
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
```

* offer方法，重要的属性limit，这个就是队列的上限，当触达上限时，就会触发拒绝策略。

* take方法也是同步方法，线程不断从runnableList获取，没有就阻塞。

* size方法，返回队列中的任务个数。

# 初始化任务线程池




