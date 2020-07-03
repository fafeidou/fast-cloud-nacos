
# 前言

如果子线程想要拿到父线程的中的ThreadLocal值怎么办呢？看下下面代码

```java

public class ThreadLocalParentChild {

    public static void main(String[] args) {

        final ThreadLocal threadLocal = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return "abc";
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(threadLocal.get());//NULL
            }
        }).start();
    }

}

```

子线程获取的时候会使null，这个问题`InheritableThreadLocal`已经帮我们解决了

#  InheritableThreadLocal

把ThreadLocal换成InheritableThreadLocal,发现问题已经解决了。

```java

public class InheritableThreadLocalParentChild {

    public static void main(String[] args) {

        final InheritableThreadLocal threadLocal = new InheritableThreadLocal() {
            @Override
            protected Object initialValue() {
                return "abc";
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(threadLocal.get());
            }
        }).start();
    }

}

```

可以看到输出的是abc，下面来看下它的源码

```java

public class InheritableThreadLocal<T> extends ThreadLocal<T> {
    /**
     * Computes the child's initial value for this inheritable thread-local
     * variable as a function of the parent's value at the time the child
     * thread is created.  This method is called from within the parent
     * thread before the child is started.
     * <p>
     * This method merely returns its input argument, and should be overridden
     * if a different behavior is desired.
     *
     * @param parentValue the parent thread's value
     * @return the child thread's initial value
     */
    protected T childValue(T parentValue) {
        return parentValue;
    }

    /**
     * Get the map associated with a ThreadLocal.
     *
     * @param t the current thread
     */
    ThreadLocalMap getMap(Thread t) {
       return t.inheritableThreadLocals;
    }

    /**
     * Create the map associated with a ThreadLocal.
     *
     * @param t the current thread
     * @param firstValue value for the initial entry of the table.
     */
    void createMap(Thread t, T firstValue) {
        t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
    }
}
```

可以看到重写了getMap和createMap方法，inheritableThreadLocals这个变量是一个新的变量，记得ThreadLocal里面用的是threadLocals，这两个变量
都是Thread中的变量，我们可以看下JDK都干了什么事。

下面是Thread源码中的代码片段

```java

 /* ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
    ThreadLocal.ThreadLocalMap threadLocals = null;

    /*
     * InheritableThreadLocal values pertaining to this thread. This map is
     * maintained by the InheritableThreadLocal class.
     */
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
```
确实是两个变量，那就有个问题，子线程是如何拿到父线程中的变量的呢？

我们在new Thread的时候看都做什么，一步一步来

```java

    public Thread() {
        init(null, null, "Thread-" + nextThreadNum(), 0);
    }

    private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize) {
        init(g, target, name, stackSize, null, true);
    }

     private void init(ThreadGroup g, Runnable target, String name,
                      long stackSize, AccessControlContext acc,
                      boolean inheritThreadLocals) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;

        Thread parent = currentThread();
        SecurityManager security = System.getSecurityManager();
        if (g == null) {
            /* Determine if it's an applet or not */

            /* If there is a security manager, ask the security manager
               what to do. */
            if (security != null) {
                g = security.getThreadGroup();
            }

            /* If the security doesn't have a strong opinion of the matter
               use the parent thread group. */
            if (g == null) {
                g = parent.getThreadGroup();
            }
        }

        /* checkAccess regardless of whether or not threadgroup is
           explicitly passed in. */
        g.checkAccess();

        /*
         * Do we have the required permissions?
         */
        if (security != null) {
            if (isCCLOverridden(getClass())) {
                security.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
            }
        }

        g.addUnstarted();

        this.group = g;
        this.daemon = parent.isDaemon();
        this.priority = parent.getPriority();
        if (security == null || isCCLOverridden(parent.getClass()))
            this.contextClassLoader = parent.getContextClassLoader();
        else
            this.contextClassLoader = parent.contextClassLoader;
        this.inheritedAccessControlContext =
                acc != null ? acc : AccessController.getContext();
        this.target = target;
        setPriority(priority);
        //inheritThreadLocals=true，上面传过来的，如果父线程的inheritableThreadLocals不为null，就浅拷贝一份
        if (inheritThreadLocals && parent.inheritableThreadLocals != null)
            this.inheritableThreadLocals =
                ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
        /* Stash the specified stack size in case the VM cares */
        this.stackSize = stackSize;

        /* Set thread ID */
        tid = nextThreadID();
    }
```
一步一步的调用，最终到了init方法上，如果父线程的inheritableThreadLocals不为null，就浅拷贝一份给当前线程，所以子线程就可以拿到
父线程的值了。但是这样就不会有问题了么，我们真正开发的时候用的是线程池，我们来试下

```java

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

```

```shell script
aaa
========
aaa
bbb
========
bbb
bbb
========
aaa
```

线程池运行了两次，看第二次的结果，我们拿到的数据都是bbb了，这就有问题了，这就说明线程池里面的线程是缓存的，线程结束后，没有清除ThreadLocalMap中的内容，
下次这个再次提交任务的时候，取得还是线程池中缓存的线程，输出的还是上次的ThreadLocalMap中的内容，于是就出现了拿父线程的ThreadLocal变量就错了。

面对这种问题，经过搜索阿里transmittable-thread-local可以解决

# transmittable-thread-local


github地址: https://github.com/alibaba/transmittable-thread-local

看了阿里的解决方案之后，我们再来试下

首先得引入pom

```xml
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>transmittable-thread-local</artifactId>
            <version>2.6.1</version>
        </dependency>
```

再来看下测试代码

```java

public class ThreadPoolTransmittable {

    public static void main(String[] args) throws InterruptedException {
        final TransmittableThreadLocal transmittableThreadLocal = new TransmittableThreadLocal();
        transmittableThreadLocal.set("aaa");
        //输出 aaa
        System.out.println(transmittableThreadLocal.get());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("========");
                System.out.println(transmittableThreadLocal.get());
                transmittableThreadLocal.set("bbb");
                System.out.println(transmittableThreadLocal.get());
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(executorService);
        ttlExecutorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        ttlExecutorService.submit(runnable);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("========");
        System.out.println(transmittableThreadLocal.get());
    }
}

```

```shell script
aaa
========
aaa
bbb
========
aaa
bbb
========
aaa
```

可以看到第二次任务先输出aaa，在输出bbb是我们想要的结果。

它的原理其实就是线程执行之前进行复制父线程的线程变量，线程结束之后清楚了父线程线程变量。说的比较笼统，如果有更好的解释，欢迎下方评论。

#  总结

开发过程中，我们用阿里的还是比较多的，推荐这种用法，最后看了官方文档，看到了使用了java agent技术，这个东西调研下，准备在下篇文章输出。
