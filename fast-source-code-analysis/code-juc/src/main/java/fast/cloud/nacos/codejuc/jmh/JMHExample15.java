package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.log;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@State(Scope.Thread)
public class JMHExample15 {
    private final double x1 = 124.456;
    private final double x2 = 342.456;

    private double y1 = 124.456;
    private double y2 = 342.456;

    @Benchmark
    public double returnDirect() {
        return 42_620.79997;
    }

    @Benchmark
    public double returnCaculate_1() {
        return x1 * x2;
    }

    @Benchmark
    public double returnCaculate_2() {
        return log(y1) * log(y2);
    }

    @Benchmark
    public double returnCaculate_3() {
        return log(x1) * log(x2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample15.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
