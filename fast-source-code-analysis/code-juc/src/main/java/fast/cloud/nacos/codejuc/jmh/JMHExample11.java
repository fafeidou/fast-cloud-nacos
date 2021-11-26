package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@Threads(5)
@State(Scope.Thread)
public class JMHExample11 {
    //定义了list但是没有初始化
    private List<String> list;

    @Setup
    public void setup() {
        list = new ArrayList<>();
    }

    @Benchmark
    public void measureRight() {
        list.add("test");
    }

    @Benchmark
    public void measureWrong() {

    }

    @TearDown
    public void tearDown() {
        assert list.size() > 0 : "muster greeter than zero";
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample11.class.getSimpleName())
                .forks(1)
                .jvmArgs("-ea")
//                .timeUnit(TimeUnit.NANOSECONDS)
//                .measurementIterations(10)
//                .warmupIterations(10)
                .build();
        new Runner(opts).run();
    }

}
