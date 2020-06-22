package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 11:20
 */
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
