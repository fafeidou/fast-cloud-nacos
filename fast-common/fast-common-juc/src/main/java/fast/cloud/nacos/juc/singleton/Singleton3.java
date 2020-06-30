package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton3 {

    private byte[] data = new byte[1024];

    private static Singleton3 instance = null;

    private Singleton3() {
        System.out.println("Singleton3 实例化");
    }

    //加锁，每次只有一个线程获得
    public static synchronized Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
