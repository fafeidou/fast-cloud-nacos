package fast.cloud.nacos.codejuc.jmh;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
public class JMHExample4 {
    @BenchmarkMode(Mode.AverageTime)
    @Benchmark
    public void testAverageTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @BenchmarkMode(Mode.Throughput)
    @Benchmark
    public void testThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @BenchmarkMode(Mode.SampleTime)
    @Benchmark
    public void testSampleTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @BenchmarkMode(Mode.SingleShotTime)
    @Benchmark
    public void testSingleShotTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @BenchmarkMode({Mode.AverageTime,Mode.Throughput})
    @Benchmark
    public void testAverageTimeAndThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @BenchmarkMode(Mode.All)
    @Benchmark
    public void testAll() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample4.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
