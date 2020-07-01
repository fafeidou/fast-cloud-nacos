ThreadLocal源码分析及使用

@[toc]

# 前言

在有些时候，单个线程执行任务非常多的时候，后一个步输入是前一个步骤的输出，我们有时候会采用责任链模式，但是调用链路长了以后，这种传参方式
会显得冗余，于是就有了线程上下文的设计，每个线程会有不同的参数实例。原因是每个线程Thread.currentThread()作为key，这样就可以保证线程的
独立性。

> 需要注意的是，我们会一般用Map存储，用当前的线程作为key，当线程生命周期结束后，Map中的实例并不会释放，因为还是根可达的，久而久之，会产生
>内存溢出。

# ThreadLocal 简单介绍


在展开深入分析之前，咱们先来看一个官方示例：

```java

public class ThreadId {

    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> threadId =
        new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return nextId.getAndIncrement();
            }
        };

    // Returns the current thread's unique ID, assigning it if necessary
    public static int get() {
        return threadId.get();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("threadName=" + Thread.currentThread().getName() + ",threadId=" + ThreadId.get());
                }
            }).start();
        }
    }

}

```
运行如下，
```shell script
threadName=Thread-2,threadId=0
threadName=Thread-3,threadId=1
threadName=Thread-0,threadId=2
threadName=Thread-1,threadId=3
threadName=Thread-4,threadId=4

```

可以看到每个线程不会相互影响，每个线程存入threadLocal的值是完全互相独立的。


我再把类注释放上去

```shell script
 * This class provides thread-local variables.  These variables differ from
 * their normal counterparts in that each thread that accesses one (via its
 * {@code get} or {@code set} method) has its own, independently initialized
 * copy of the variable.  {@code ThreadLocal} instances are typically private
 * static fields in classes that wish to associate state with a thread (e.g.,
 * a user ID or Transaction ID).
```
大概意思就是几条：

* 它能让线程拥有了自己内部独享的变量

* 每一个线程可以通过get、set方法去进行操作

* 可以覆盖initialValue方法指定线程独享的值

* 通常会用来修饰类里private static final的属性，为线程设置一些状态信息，例如user ID或者Transaction ID

* 每一个线程都有一个指向threadLocal实例的弱引用，只要线程一直存活或者该threadLocal实例能被访问到，都不会被垃圾回收清理掉


# ThreadLocal常用方法介绍
ThreadLocal方法有如下：

>ThreadLocal、
childValue、
createInheritedMap、
createMap、
get、
getMap、
initialValue、
nextHashCode、
remove、
set、
setInitialValue、
withInitial、
HASH_INCREMENT、
nextHashCode、
threadLocalHashCode

ThreadLocal常用方法有initialValue、get、set、remove。

## initialValue

```java

    protected T initialValue() {
        return null;
    }

```

就是在为每个线程变量赋一个默认值，如果没有设的话，默认为null。

## set

就是如果没有调用set方法，数据的初始值就是initialValue的值。


```java

   public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }

   void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

   private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);

            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                ThreadLocal<?> k = e.get();

                if (k == key) {
                    e.value = value;
                    return;
                }

                if (k == null) {
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehash();
    }

```


* 获取当前现成的Thread.currentThread()

* 根据当前线程获取到线程的ThreadLocalMap，可以看下面Thread源码，
每个线程有一个ThreadLocalMap的引用，先记下，后面有用。

```java

public
class Thread implements Runnable {
/* ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
    ThreadLocal.ThreadLocalMap threadLocals = null;
}

```

* 如果没有获取到就创建，获取到就赋值

* 先说下这个创建map的方法，首先会new一个ThreadLocalMap，创建entry，用当前的ThreadLocal作为key，要存的数据作为value,结束

* 如果map存在，就遍历entry，找到如果有相同的ThreadLocal，那就用新的值替换掉，结束。

* 如果没有找到相同的ThreadLocal，则创建entry，用当前的ThreadLocal作为key，要存的数据作为value

> 注意，如果遍历过程中，entry中key的值为null，那就用新的key替代，还会根据当前的size和阈值进行比较，超过阈值，则进行key值为null的清理。

## get

```java

    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }

```

* 获取当前线程内部的ThreadLocalMap

* map存在则获取当前ThreadLocal对应的value值

* map不存在或者找不到value值，则调用setInitialValue，进行初始化

# 看看ThreadLocalMap

```java

    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }

```

`Entry`是`WeakReference`类型的，Java垃圾回收时，看一个对象需不需要回收，就是看这个对象是否可达。什么是可达，
就是能不能通过引用去访问到这个对象。

>jdk1.2以后，引用就被分为四种类型：强引用、弱引用、软引用和虚引用。强引用就是我们常用的Object obj = new Object()，obj就是一个强引用，指向了对象内存空间。
>当内存空间不足时，Java垃圾回收程序发现对象有一个强引用，宁愿抛出OutofMemory错误，也不会去回收一个强引用的内存空间。而弱引用，即WeakReference，
>意思就是当一个对象只有弱引用指向它时，垃圾回收器不管当前内存是否足够，都会进行回收。反过来说，这个对象是否要被垃圾回收掉，取决于是否有强引用指向
>。ThreadLocalMap这么做，是不想因为自己存储了ThreadLocal对象，而影响到它的垃圾回收，而是把这个主动权完全交给了调用方，一旦调用方不想使用，
>设置ThreadLocal对象为null，内存就可以被回收掉。


## 看个例子

```java

public class MemoryLeak {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    TestClass t = new TestClass(i);
                    t.printId();
                    t = null;
                }
            }
        }).start();
    }


    static class TestClass{
        private int id;
        private int[] arr;
        private ThreadLocal<TestClass> threadLocal;
        TestClass(int id){
            this.id = id;
            arr = new int[1000000];
            threadLocal = new ThreadLocal<>();
            threadLocal.set(this);
        }

        public void printId(){
            System.out.println(threadLocal.get().id);
        }
    }
}

```

运行的结果为： `java.lang.OutOfMemoryError: Java heap space`

稍作修改

```java
public class MemoryLeak {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    TestClass t = new TestClass(i);
                    t.printId();
                    t.threadLocal.remove();
                }
            }
        }).start();
    }


    static class TestClass{
        private int id;
        private int[] arr;
        private ThreadLocal<TestClass> threadLocal;
        TestClass(int id){
            this.id = id;
            arr = new int[1000000];
            threadLocal = new ThreadLocal<>();
            threadLocal.set(this);
        }

        public void printId(){
            System.out.println(threadLocal.get().id);
        }
    }
}

```
正常完成，比代码只有一处不同：t = null改为了t.threadLocal.remove();

我们来看下remove的源码

```java

     public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }

```

就一句话，获取当前线程内部的ThreadLocalMap，存在则从map中删除这个ThreadLocal对象。

分析下为什么？

```java
   ThreadLocalMap.class

    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }

```

Entry中的key指向的是ThreadLocal，说明key是软引用，当线程的周期结束后，会自动回收，也就是key这时为null，
但是value是Object，这个是强引用，没有办法回收，这也是之前看set方法源码的时候，会清除key为null的数据，我们在
实际应用的时候记得线程结束后，调用remove方法。

