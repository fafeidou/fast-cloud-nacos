package fast.cloud.nacos.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class MeterExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private final static Meter requestMeter = registry.meter("tqs");

    private final static Meter sizeMeter = registry.meter("volume");

    public static void main(String[] args) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES).build();
        reporter.start(10, TimeUnit.SECONDS);
        for (; ; ) {
            upload(new byte[current().nextInt(1000)]);
            randomSleep();
        }
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void upload(byte[] bytes) {
        requestMeter.mark();
        sizeMeter.mark(bytes.length);
    }



}
