package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10)
@Warmup(iterations = 5)
@Threads(5)
//设置为线程共享资源
@State(Scope.Benchmark)
public class JMHExample9 {
    private Map<Long, Long> concurrentMap;
    private Map<Long, Long> synchronizedMap;

    @Setup
    public void setup() {
        concurrentMap = new ConcurrentHashMap<>();
        synchronizedMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Benchmark
    public void testConCurrentMap() {
        concurrentMap.put(System.nanoTime(), System.nanoTime());
    }

    @Benchmark
    public void testSynchronizedMap() {
        synchronizedMap.put(System.nanoTime(), System.nanoTime());
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample9.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
