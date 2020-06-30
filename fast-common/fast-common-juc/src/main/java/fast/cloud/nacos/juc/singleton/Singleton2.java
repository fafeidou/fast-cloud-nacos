package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton2 {
    private byte[] data = new byte[1024];

    private static Singleton2 instance = null;

    private Singleton2() {
        System.out.println("Singleton2 实例化");
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
