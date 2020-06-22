package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 9:32
 */
public interface ReadWriteLock {

    Lock readLock();

    Lock writeLock();

    int getWaitingWriters();

    int getWritingReaders();

    int getReadingReaders();

    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }

    static ReadWriteLock readWriteLock(Boolean preferWrite) {
        return new ReadWriteLockImpl(preferWrite);
    }
}
