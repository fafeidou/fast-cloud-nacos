package fast.cloud.nacos.cat.monitor.prometheus;

import io.micrometer.core.instrument.*;
import io.micrometer.core.lang.Nullable;

import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import static fast.cloud.nacos.cat.monitor.constants.DataTypeEnum.BASE;

public class BaseMonitorClient extends MonitorClient{
    public static Counter.Builder counterBuilder(String name) {
        return counterBuilder(name, tags());
    }

    public static <T> Gauge.Builder<T> gaugeBuilder(String name, @Nullable T obj, ToDoubleFunction<T> f) {
        return gaugeBuilder(name, obj, f, tags());
    }

    public static Gauge.Builder<Supplier<Number>> gaugeBuilder(String name, Supplier<Number> f) {
        return gaugeBuilder(name, f, tags());
    }

    public static DistributionSummary.Builder histogramBuilder(String name) {
        return histogramBuilder(name, tags());
    }

    public static DistributionSummary.Builder summaryBuilder(String name) {
        return summaryBuilder(name, tags());
    }

    public static Timer.Builder histogramTimerBuilder(String name) {
        return histogramTimerBuilder(name, tags());
    }

    public static Timer.Builder summaryTimerBuilder(String name) {
        return summaryTimerBuilder(name, tags());
    }

    private static Tags tags() {
        return BASE.getTags();
    }
}
