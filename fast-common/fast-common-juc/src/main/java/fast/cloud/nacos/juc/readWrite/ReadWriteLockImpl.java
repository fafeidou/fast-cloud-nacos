package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 9:34
 */
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
        return new ReadLock(this);
    }

    @Override
    public Lock writeLock() {
        return new WriteLock(this);
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
