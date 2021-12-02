package fast.cloud.nacos.metrics;

import com.codahale.metrics.*;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class SlidingTimeWindowReservoirsHistogramExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();
    private static final Histogram histogram = new Histogram(new SlidingTimeWindowReservoir(30,TimeUnit.SECONDS));

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);

        registry.register("SlidingWindowReservoir-Histogram", histogram);

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
            TimeUnit.SECONDS.sleep(current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
