package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@State(Scope.Thread)
public class JMHExample17 {
    interface Inc{
        int inc ();
    }

    public static class Inc1 implements Inc {
        private int i = 0;
        @Override
        public int inc() {
            return ++i;
        }
    }

    public static class Inc2 implements Inc {
        private int i = 0;
        @Override
        public int inc() {
            return ++i;
        }
    }

    private Inc inc1 = new Inc1();
    private Inc inc2 = new Inc2();

    private int measure(Inc inc) {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            result += inc.inc();
        }
        return result;
    }

    @Benchmark
    public int measure_inc_1() {
        return this.measure(inc1);
    }

    @Benchmark
    public int measure_inc_2() {
        return this.measure(inc2);
    }

    @Benchmark
    public int measure_inc_3() {
        return measure(inc1);
    }

    public static void main(String[] args) throws RunnerException {

        Options opts = new OptionsBuilder()
                .include(JMHExample17.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
