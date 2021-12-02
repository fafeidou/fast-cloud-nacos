package fast.cloud.nacos.metrics.reporter;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class CsvReporterExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final CsvReporter reporter = CsvReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build(new File("/Users/qinfuxiang/Downloads"));
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
