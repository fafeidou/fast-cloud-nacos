package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class CacheGauge {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();


    public static void main(String[] args) throws InterruptedException {
        reporter.start(10,TimeUnit.SECONDS);

        registry.gauge("cache-db-size", () -> new CachedGauge(30,TimeUnit.SECONDS) {
            @Override
            protected Object loadValue() {
                return queryFromDbSize();
            }
        });

        Thread.currentThread().join();
    }

    private static Object queryFromDbSize() {
        System.out.println("=====queryFromDbSize====");
        return System.currentTimeMillis();
    }
}
