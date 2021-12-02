package fast.cloud.nacos.metrics.reporter;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class SLF4jReporterExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
            .outputTo(LoggerFactory.getLogger(SLF4jReporterExample.class))
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();

    private static final Histogram histogram = registry.histogram("search-result");

    public static void main(String[] args) {
        reporter.start(10,TimeUnit.SECONDS);

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
