package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 5)
@Warmup(iterations = 5)
@State(Scope.Thread)
public class JMHExample14 {

    double x1 = Math.PI;
    double x2 = Math.PI * 2;

    @Benchmark
    public double baseLine() {
        return Math.pow(x1, 2);
    }

    @Benchmark
    public double powButReturn() {
        Math.pow(x1, 2);
        return Math.pow(x2, 2);
    }

    @Benchmark
    public double powThenAdd() {
        return Math.pow(x1, 2) + Math.pow(x2, 2);
    }

    @Benchmark
    public void useBlackHole(Blackhole hole) {
        hole.consume(Math.pow(x1, 2));
        hole.consume(Math.pow(x2, 2));
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample14.class.getSimpleName())
                .build();
        new Runner(opts).run();
    }
}
