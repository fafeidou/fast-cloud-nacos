package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 */
public class JvmInstrumentationExample {
    public final static MetricRegistry metricRegistry = new MetricRegistry();
    public final static ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
    public static void main(String[] args) throws InterruptedException {
        consoleReporter.start(10, TimeUnit.SECONDS);
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        metricRegistry.registerAll(new ThreadStatesGaugeSet());
        metricRegistry.registerAll(new ClassLoadingGaugeSet());
        Thread.currentThread().join();
    }
}
