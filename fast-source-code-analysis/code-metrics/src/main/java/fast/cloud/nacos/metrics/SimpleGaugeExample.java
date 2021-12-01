package fast.cloud.nacos.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class SimpleGaugeExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();

    private static Queue<Long> queue = new LinkedBlockingDeque<>();


    public static void main(String[] args) {
        registry.register(MetricRegistry.name(SimpleGaugeExample.class), (Gauge<Integer>)queue::size);
        reporter.start(1,TimeUnit.SECONDS);
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.add(System.nanoTime());
            }
        }).start();

        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.poll();
            }
        }).start();
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
