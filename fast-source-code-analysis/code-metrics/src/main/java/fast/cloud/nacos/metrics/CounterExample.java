package fast.cloud.nacos.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class CounterExample {
    private static final MetricRegistry registry = new MetricRegistry();

    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS).build();
    private static final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(10, TimeUnit.SECONDS);
        Counter counter = registry.counter("queue-count", Counter::new);
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.add(System.nanoTime());
                counter.inc();
            }
        });
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                if (queue.poll() != null) {
                    counter.dec();
                }
            }
        });

        Thread.currentThread().join();
    }


    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(current().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
