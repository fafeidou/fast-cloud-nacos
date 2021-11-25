package fast.cloud.nacos.codejuc.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 代替ReentrantReadWriteLock
 * @author qinfuxiang
 */
public class StampedLockExample2 {
    private static int shareData = 0;
    private static final StampedLock lock = new StampedLock();

    public static void inc() {
        long stamp = lock.writeLock();

        try {
            shareData++;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public static int get() {
        long stamp = lock.readLock();
        try {
            return shareData;
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
