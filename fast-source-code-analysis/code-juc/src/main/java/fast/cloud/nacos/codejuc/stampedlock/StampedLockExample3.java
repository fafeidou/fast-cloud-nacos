package fast.cloud.nacos.codejuc.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 乐观锁模式
 *
 * @author qinfuxiang
 */
public class StampedLockExample3 {
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
        // 1 该方法会立即返回，并不会导致当前线程阻塞，
        // 如果获取成功，会返回一个非零的时间戳，失败返回为0
        long stamp = lock.tryOptimisticRead();
        // 2
        if (!lock.validate(stamp)) {
            // 3
            stamp = lock.readLock();
            try {
                return shareData;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        //4
        return shareData;
    }
}
