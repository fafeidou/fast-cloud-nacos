package fast.cloud.nacos.warmup;

public interface WarmUpComponent {
    default String name() {
        return this.getClass().getSimpleName();
    }

    default boolean ignoreWarmUpFail() {
        return true;
    }

    void warmUp() throws Exception;
}