package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@State(Scope.Thread)
public class JMHExample16 {
    private int x = 1;
    private int y = 2;

    @Benchmark
    public int measure() {
        return (x + y);
    }

    public int loopCompute(int times) {
        int result = 0;
        for (int i = 0; i < times; i++) {
            result += (x + y);
        }
        return result;
    }

    @OperationsPerInvocation
    @Benchmark
    public int measureLoop_1() {
        return loopCompute(1);
    }

    @OperationsPerInvocation(10)
    @Benchmark
    public int measureLoop_10() {
        return loopCompute(10);
    }

    @OperationsPerInvocation(100)
    @Benchmark
    public int measureLoop_100() {
        return loopCompute(100);
    }

    @OperationsPerInvocation(1000)
    @Benchmark
    public int measureLoop_1000() {
        return loopCompute(1000);
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample16.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
