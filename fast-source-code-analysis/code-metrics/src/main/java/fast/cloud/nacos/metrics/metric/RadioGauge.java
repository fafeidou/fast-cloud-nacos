package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class RadioGauge {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();

    private final static Meter totalMeter = new Meter();

    private final static Meter successMeter = new Meter();

    public static void main(String[] args) {
        reporter.start(10,TimeUnit.SECONDS);

        registry.gauge("success-rate", () -> new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(successMeter.getCount(), totalMeter.getCount());
            }
        });
        for (; ; ) {
            shortSleep();
            business();
        }
    }

    private static void business() {
        totalMeter.mark();
        try {
            int x = 10 / current().nextInt(6);
            successMeter.mark();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(6));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
