package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton4 {
    private byte[] data = new byte[1024];

    private static Singleton4 instance = null;

    private Singleton4() {
        System.out.println("Singleton4 实例化");
    }

    public static  Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}
