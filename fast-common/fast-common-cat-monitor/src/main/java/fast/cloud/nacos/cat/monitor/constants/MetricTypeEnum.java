package fast.cloud.nacos.cat.monitor.constants;


import io.micrometer.core.instrument.Tags;

public enum MetricTypeEnum {
    /**
     * counter
     */
    COUNTER(Tags.of("metric_type", "counter")),
    GAUGE(Tags.of("metric_type", "gauge")),
    HISTOGRAM(Tags.of("metric_type", "histogram")),
    SUMMARY(Tags.of("metric_type", "summary"));
    private final Tags tags;

    MetricTypeEnum(Tags tags) {
        this.tags = tags;
    }

    public Tags getTags() {
        return tags;
    }
}
