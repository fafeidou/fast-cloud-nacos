package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class JMHExample1 {
    private final static String DATA_STATE = "DUMMY DATA";
    private List<String> arrayList;
    private List<String> linkedList;

    @Setup(Level.Iteration)
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
    }

    @Benchmark
    public List<String> arrayListAdd() {
        arrayList.add(DATA_STATE);
        return arrayList;
    }

    @Benchmark
    public List<String> linkedListAdd() {
        linkedList.add(DATA_STATE);
        return linkedList;
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder().include(JMHExample1.class.getSimpleName()).forks(1).measurementIterations(10).warmupIterations(10).build();
        new Runner(opts).run();
    }
}
