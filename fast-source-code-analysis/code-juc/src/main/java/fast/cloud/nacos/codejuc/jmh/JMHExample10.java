package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@Threads(5)
@State(Scope.Benchmark)
public class JMHExample10 {
    @Param({"1", "2", "3", "4"})
    private int type;

    private Map<Long, Long> map;

    @Setup
    public void setup() {
        switch (type) {
            case 1:
                map = new ConcurrentHashMap<>();
                break;
            case 2:
                map = new ConcurrentSkipListMap<>();
                break;
            case 3:
                map = new Hashtable<>();
                break;
            case 4:
                map = Collections.synchronizedMap(new HashMap<>());
                break;
            default:
                throw new IllegalArgumentException("illegal map type.");
        }
    }

    @Benchmark
    public void test() {
        map.put(System.nanoTime(), System.nanoTime());
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample10.class.getSimpleName())
                .forks(1)
//                .timeUnit(TimeUnit.NANOSECONDS)
//                .measurementIterations(10)
//                .warmupIterations(10)
                .build();
        new Runner(opts).run();
    }
}
