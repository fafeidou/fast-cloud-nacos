package fast.cloud.nacos.codejuc.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.PI;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 5)
@Warmup(iterations = 5)
@Threads(5)
@State(Scope.Thread)
public class JMHExample13 {

    @Benchmark
    public void baseLine() {
        //空方法
    }

    @Benchmark
    public void measureLog1() {
        // 进行数学运算、但是在局部方法内
        Math.log(PI);
    }

    @Benchmark
    public void measureLog2() {
        //result 是通过数学运算所得并且下一行代码中得到了使用
        double result = Math.log(PI);
        //对result进行数学运算，但是结果不保存也不返回，更不会进行第二次运算
        Math.log(result);
    }

    @Benchmark
    public double measureLog3() {
        return Math.log(PI);
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(JMHExample13.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opts).run();
    }
}
