package fast.cloud.nacos.grpc.starter.fallback;

public interface FallbackFactory<T> {
    T create();

    public static final class Default<T> implements FallbackFactory<T> {
        public Default(T constant) {
        }

        @Override
        public T create() {
            return null;
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
