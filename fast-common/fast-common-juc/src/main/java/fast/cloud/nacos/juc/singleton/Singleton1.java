package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton1 {

    private byte[] data = new byte[1024];

    private static Singleton1 instance = new Singleton1();

    private Singleton1() {
        System.out.println("Singleton1 实例化");
    }

    public static Singleton1 getInstance() {
        return instance;
    }
}
