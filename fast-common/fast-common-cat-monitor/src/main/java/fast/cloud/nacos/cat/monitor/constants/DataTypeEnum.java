package fast.cloud.nacos.cat.monitor.constants;

import io.micrometer.core.instrument.Tags;

public enum DataTypeEnum {
    /**
     * base business
     */
    BASE(Tags.of("data_type", "base")),
    BUSINESS(Tags.of("data_type", "biz"));
    private final Tags tags;

    DataTypeEnum(Tags tags) {
        this.tags = tags;
    }

    public Tags getTags() {
        return tags;
    }
}