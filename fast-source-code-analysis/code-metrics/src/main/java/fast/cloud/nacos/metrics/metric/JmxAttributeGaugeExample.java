package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

public class JmxAttributeGaugeExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        reporter.start(10,TimeUnit.SECONDS);
        registry.register(MetricRegistry.name(JmxAttributeGaugeExample.class, "HeapMemory"), new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));
        registry.register(MetricRegistry.name(JmxAttributeGaugeExample.class, "NonHeapMemory"), new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));
        Thread.currentThread().join();
    }
}
