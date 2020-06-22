package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 11:19
 */
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
        synchronized (readWriteLock.getMUTEX()) {
            //使当前reading数量减一
            readWriteLock.decrementReadingReaders();
            //让writer线程中能够获得机会
            readWriteLock.setPreferWriter(true);
            //通知唤醒 mutex中的wait set中的线程
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}
