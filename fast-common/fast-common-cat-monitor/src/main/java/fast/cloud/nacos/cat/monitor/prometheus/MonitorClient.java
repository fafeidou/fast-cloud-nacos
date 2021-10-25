package fast.cloud.nacos.cat.monitor.prometheus;

import io.micrometer.core.instrument.*;

import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import static fast.cloud.nacos.cat.monitor.constants.MetricTypeEnum.*;

public class MonitorClient {
    protected static Gauge.Builder<Supplier<Number>> gaugeBuilder(String name, Supplier<Number> f, Tags tags) {
        return Gauge.builder(name, f).tags(GAUGE.getTags().and(tags));
    }

    protected static <T> Gauge.Builder<T> gaugeBuilder(String name, T obj, ToDoubleFunction<T> f, Tags tags) {
        return Gauge.builder(name, obj, f).tags(GAUGE.getTags().and(tags));
    }

    protected static DistributionSummary.Builder summaryBuilder(String name, Tags tags) {
        return DistributionSummary.builder(name)
                .tags(SUMMARY.getTags().and(tags));
    }

    protected static DistributionSummary.Builder histogramBuilder(String name, Tags tags) {
        return DistributionSummary.builder(name).publishPercentileHistogram()
                .tags(HISTOGRAM.getTags().and(tags));
    }

    protected static Counter.Builder counterBuilder(String name, Tags tags) {
        return Counter.builder(name).tags(COUNTER.getTags().and(tags));
    }

    protected static Timer.Builder histogramTimerBuilder(String name, Tags tags) {
        return Timer.builder(name)
                .tags(HISTOGRAM.getTags().and(tags))
                .publishPercentileHistogram();
    }
    protected static Timer.Builder summaryTimerBuilder(String name, Tags tags) {
        return Timer.builder(name)
                .tags(SUMMARY.getTags().and(tags));
    }
}
