package fast.cloud.nacos.metrics.reporter;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class JmxReporterExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final JmxReporter reporter = JmxReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();
    private static final Histogram histogram = registry.histogram("search-result");

    public static void main(String[] args) {
        reporter.start();

        while (true) {
            doSearch();
            randomSleep();
        }
    }

    private static void doSearch() {
        histogram.update(current().nextInt(10));
    }


    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
