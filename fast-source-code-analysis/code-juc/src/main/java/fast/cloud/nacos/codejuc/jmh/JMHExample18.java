package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@State(Scope.Thread)
public class JMHExample18 {

    private BlockingQueue<Integer> queue;

    private final static int VALUE = Integer.MAX_VALUE;

    @Setup
    public void init() {
        this.queue = new ArrayBlockingQueue<>(10);
    }


    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public void put() throws InterruptedException {
        queue.put(VALUE);
    }

    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public int get() throws InterruptedException {
        return queue.take();
    }

    public static void main(String[] args) throws RunnerException {

        Options opts = new OptionsBuilder()
                .include(JMHExample18.class.getSimpleName())
                .timeout(TimeValue.seconds(10))
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
