package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JmhHello {

    String string = "";
    StringBuilder stringBuilder = new StringBuilder();

    @Benchmark
    public String stringAdd() {
        for (int i = 0; i < 1000; i++) {
            string = string + i;
        }
        return string;
    }

    @Benchmark
    public String stringBuilderAppend() {
        for (int i = 0; i < 1000; i++) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhHello.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}

