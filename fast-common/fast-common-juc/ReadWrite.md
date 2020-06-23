# 读写锁分离设计模式

对资源的访问一般包括两种动作--读和写，多线程同一时刻对资源读操作，虽然有资源竞争，但是这种竞争不足以引起数据不一致，那么这个时候直接采用排他锁的方式，
就显得有些粗暴了。

如果对某个资源读的操作多于写的操作，那么多线程读时并不需要加锁，很明显对程序性能的提升有很大的帮助，于是想搞一个读写锁的简单实现。


## Lock

```$xslt

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
```
* lock() : 获取显示锁，没有获取锁的线程将要阻塞。

* unlock() : 释放锁,减少reader和writer的数量。


##  ReadWriteLock

```$xslt

public interface ReadWriteLock {

    Lock readLock();

    Lock writeLock();

    int getWaitingWriters();

    int getWaitingReaders();

    int getReadingReaders();

    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }

    static ReadWriteLock readWriteLock(Boolean preferWrite) {
        return new ReadWriteLockImpl(preferWrite);
    }
}
```

* 如果reader个数大于零，那么就意味着writer的个数等于0，反之如果writer个数大于零（实际上writer的个数最多也只能为1），则reader个数等于0

## ReadWriteLockImpl

```$xslt

public class ReadWriteLockImpl implements ReadWriteLock{

    private final Object MUTEX = new Object();

    private int writingReaders = 0;

    private int waitingWriters = 0;

    private int readingReaders = 0;

    private boolean preferWriter;

    public ReadWriteLockImpl() {
        this(true);
    }

    public ReadWriteLockImpl(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    @Override
    public Lock readLock() {
        return null;
    }

    @Override
    public Lock writeLock() {
        return null;
    }

    @Override
    public int getWaitingWriters() {
        return this.waitingWriters;
    }

    @Override
    public int getWritingReaders() {
        return this.writingReaders;
    }

    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }

    void incrementWaitingWriters() {
        this.waitingWriters ++;
    }

    void incrementWritingWriters() {
        this.waitingWriters ++;
    }

    void incrementReadingReaders() {
        this.readingReaders ++;
    }

    void decrementWaitingWriters() {
        this.waitingWriters --;
    }

    void decrementWritingWriters() {
        this.waitingWriters --;
    }

    void decrementReadingReaders() {
        this.readingReaders --;
    }

    public Object getMUTEX() {
        return MUTEX;
    }

    public boolean isPreferWriter() {
        return preferWriter;
    }

    public void setPreferWriter(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }
}

```

* 虽然在做一个读写锁，内部也需要y一个锁进行数据同步，以及线程之间的通信，其中METUX的作用就在于此。

##  ReadLock
```$xslt

public class ReadLock implements Lock{

    private final ReadWriteLockImpl readWriteLock;

    ReadLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }



    @Override
    public void lock() throws InterruptedException {
        synchronized (readWriteLock.getMUTEX()) {
            while (readWriteLock.getWritingReaders() > 0 || (readWriteLock.isPreferWriter()
                && readWriteLock.getWritingReaders() > 0)) {
                readWriteLock.getMUTEX().wait();
            }
        }
        readWriteLock.incrementReadingReaders();
    }

    @Override
    public void unlock() {
        //使当前reading数量减一
        readWriteLock.decrementReadingReaders();
        //让writer线程中能够获得机会
        readWriteLock.setPreferWriter(true);
        //通知唤醒 mutex中的wait set中的线程
        readWriteLock.getMUTEX().notifyAll();
    }
}

```

*  当没有任何线程对数据进行操作的时候，读线程才有可能获取锁的拥有权，当然为了公平起见，如果有很多线程在等待写锁的拥有权，同样读线程会进入
mutex中的wait set 中。

*  读线程释放锁，这意味着reader的数量将将减少一个，同时唤醒wait中的线程

##  WriteLock

```$xslt

public class WriteLock implements Lock {

    private final ReadWriteLockImpl readWriteLock;

    WriteLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (readWriteLock.getMUTEX()) {
            try {
                readWriteLock.incrementWaitingWriters();
                while (readWriteLock.getReadingReaders() > 0 || readWriteLock.getWritingReaders() > 0) {
                    readWriteLock.getMUTEX().wait();
                }
            } finally {
                readWriteLock.decrementWaitingWriters();
            }
            readWriteLock.incrementWritingWriters();
        }
    }

    @Override
    public void unlock() {
        synchronized (readWriteLock.getMUTEX()) {
            readWriteLock.decrementWritingWriters();
            //可以使读锁最快速的获得
            readWriteLock.setPreferWriter(false);
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}

```

*  当有线程进行读操作或者写操作的时候，若当前线程试图获取锁，则进入mutex中的wait set中。

*  写锁的释放，唤醒wait中的线程，设置preferWriter为false，提高读线程获取锁的机会。

##  读写锁的使用

###  ShareData, 对读写锁的进行线程控制


```$xslt

public class ShareData {

    private final List<Character> container = new ArrayList<>();

    private final ReadWriteLock readWriteLock = ReadWriteLock.readWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final int length;

    public ShareData(int length) {
        this.length = length;
        for (int i = 0; i < length; i++) {
            container.add(i, 'c');
        }
    }

    public char[] read() throws InterruptedException {
        try {
            readLock.lock();
            char[] newBuffer = new char[length];
            for (int i = 0; i < length; i++) {
                newBuffer[i] = container.get(i);
            }
            slowly();
            return newBuffer;
        } finally {
            readLock.unlock();
        }
    }

    public void write() throws InterruptedException {
        try {
            writeLock.lock();
            for (int i = 0; i < length; i++) {
                container.add(i, 'c');
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void slowly() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
```

*  为了体现读写锁，使用了10读线程，两个写线程模拟


# 总结

* RW锁，在读多写少的情况下，可以显示的优势，如果读小于等于写的情况下，可能性能会有影响，可以参考jdk1.8的java.util.concurrent.locks.StampedLock，
该工具对读写场景的优化。

* 踩坑记录，wait/notify这些用的时候一定要注意，使用一定要加上synchronized标识，否则会抛出，java.lang.IllegalMonitorStateException，切记，切记。




