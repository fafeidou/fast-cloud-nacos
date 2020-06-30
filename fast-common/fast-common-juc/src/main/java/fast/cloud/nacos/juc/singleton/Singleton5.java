package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton5 {

    private byte[] data = new byte[1024];

    /**
     * static 和 volatile 切换，编译器不会报错
     */
    private static volatile Singleton5 instance = null;

    private Singleton5() {
        System.out.println("Singleton5 实例化");
    }

    public static Singleton5 getInstance() {
        if (instance == null) {
            synchronized (Singleton5.class) {
                if (instance == null) {
                    instance = new Singleton5();
                }
            }
        }
        return instance;
    }
}
