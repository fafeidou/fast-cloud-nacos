package fast.cloud.nacos.codejuc.stampedlock;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

@Measurement(iterations = 20)
@Warmup(iterations = 20)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JMHStampedLock {

    @State(Scope.Group)
    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final StampedLock stampedLock = new StampedLock();

        public void stampedLockInc() {
            long stamp = stampedLock.writeLock();
            try {
                x++;
            }finally {
                stampedLock.unlockWrite(stamp);
            }
        }

        public int stampedReadLockGet() {
            long stamp = stampedLock.readLock();
            try {
                return x;
            }finally {
                stampedLock.unlockRead(stamp);
            }
        }

        public int stampedOptimisticReadLockGet() {
            long stamp = stampedLock.tryOptimisticRead();
            // 2
            if (!stampedLock.validate(stamp)) {
                // 3
                stamp = stampedLock.readLock();
                try {
                    return x;
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            //4
            return x;
        }

        public void lockInc() {
            lock.lock();
            try {
                x++;
            }finally {
                lock.unlock();
            }
        }

        public int lockGet() {
            lock.lock();
            try {
                return x;
            }finally {
                lock.unlock();
            }
        }

        public void writeLockInc() {
            readWriteLock.writeLock().lock();
            try {
                x++;
            }finally {
                readWriteLock.writeLock().unlock();
            }
        }

        public int readLockGet() {
            readWriteLock.readLock().lock();
            try {
                return x;
            }finally {
                readWriteLock.readLock().unlock();
            }
        }

        @GroupThreads(5)
        @Group("lock")
        @Benchmark
        public void lockInc(Test test) {
            test.lockInc();
        }

        @GroupThreads(5)
        @Group("lock")
        @Benchmark
        public void lockGet(Test test, Blackhole blackhole) {
            blackhole.consume(test.lockGet());
        }

        @GroupThreads(5)
        @Group("rwlock")
        @Benchmark
        public void writeLockInc(Test test) {
            test.writeLockInc();
        }

        @GroupThreads(5)
        @Group("rwlock")
        @Benchmark
        public void readLockGet(Test test, Blackhole blackhole) {
            blackhole.consume(test.readLockGet());
        }

        @GroupThreads(5)
        @Group("stampedLock")
        @Benchmark
        public void stampedLockInc(Test test) {
            test.stampedLockInc();
        }

        @GroupThreads(5)
        @Group("stampedLock")
        @Benchmark
        public void stampedLockGet(Test test, Blackhole blackhole) {
            blackhole.consume(test.stampedReadLockGet());
        }

        @GroupThreads(5)
        @Group("stampedLockOptimistic")
        @Benchmark
        public void stampedLockOptimisticLockInc(Test test) {
            test.stampedLockInc();
        }

        @GroupThreads(5)
        @Group("stampedLockOptimistic")
        @Benchmark
        public void stampedLockOptimisticLockGet(Test test, Blackhole blackhole) {
            blackhole.consume(test.stampedOptimisticReadLockGet());
        }

    }

    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder().include(JMHStampedLock.class.getSimpleName()).forks(1).build();
        new Runner(build).run();
    }

}
