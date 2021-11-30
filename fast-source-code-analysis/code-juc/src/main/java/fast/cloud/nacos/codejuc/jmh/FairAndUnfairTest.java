package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@State(Scope.Group)
public class FairAndUnfairTest {

    @Param({"1", "2"})
    private int type;

    private static Lock lock;

    @Setup
    public void setup() {
        switch (type) {
            case 1:
                lock = new ReentrantLock(true);
                break;
            case 2:
                //默认就是非公平锁
                lock = new ReentrantLock(false);
                break;
            default:
                throw new IllegalArgumentException("illegal lock type.");
        }
    }

    @Benchmark
    @GroupThreads(5)
    @Group("lock")
    public void put() {
        lock.lock();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private int randomIntValue() {
        return (int) Math.ceil(Math.random() * 600000);
    }

    @Benchmark
    @GroupThreads(5)
    @Group("lock")
    public Integer get() {
        lock.lock();
        try {
            Thread.sleep(10);
            return randomIntValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return 0;
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(FairAndUnfairTest.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
