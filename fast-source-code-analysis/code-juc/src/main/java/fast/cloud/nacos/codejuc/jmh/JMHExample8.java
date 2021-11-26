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
@Threads(5)
@Fork(1)
public class JMHExample8 {
    @State(Scope.Group)
    public static class Test {
        public Test() {
            System.out.println("create instance");
        }

        public void write() {
            System.out.println("write");
        }

        public void read() {
            System.out.println("read");
        }
    }

    @Benchmark
    @GroupThreads(3)
    @Group("test")
    public void write(Test test) {
        test.write();
    }

    @Benchmark
    @GroupThreads(3)
    @Group("test")
    public void read(Test test) {
        test.read();
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample8.class.getSimpleName())
                .forks(1)
//                .timeUnit(TimeUnit.NANOSECONDS)
//                .measurementIterations(10)
//                .warmupIterations(10)
                .build();
        new Runner(opts).run();
    }
}
