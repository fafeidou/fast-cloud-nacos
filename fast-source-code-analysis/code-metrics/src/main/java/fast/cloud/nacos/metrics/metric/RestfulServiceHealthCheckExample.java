package fast.cloud.nacos.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 */
public class RestfulServiceHealthCheckExample {
    public static void main(String[] args) throws InterruptedException {
        final HealthCheckRegistry healthService = new HealthCheckRegistry();
        healthService.register("thread-dead-lock-hc", new ThreadDeadlockHealthCheck());
        healthService.register("restful-check-hc", new RestfulServiceHealthCheck());

        final MetricRegistry metricRegistry = new MetricRegistry();
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();

        metricRegistry.gauge("app-health-check",() -> healthService::runHealthChecks);

        consoleReporter.start(10, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }
}
