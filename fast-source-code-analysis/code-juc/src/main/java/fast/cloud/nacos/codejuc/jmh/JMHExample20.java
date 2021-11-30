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
@State(Scope.Group)
public class JMHExample20 {
    @Param({"1", "2", "3", "4"})
    private int type;

    private Map<Integer, Integer> map;

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
    @GroupThreads(5)
    @Group("g")
    public void put() {
        int random = randomIntValue();
        map.put(random, random);
    }

    private int randomIntValue() {
        return (int) Math.ceil(Math.random() * 600000);
    }

    @Benchmark
    @GroupThreads(5)
    @Group("g")
    public Integer get() {
        return map.get(randomIntValue());
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample20.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
