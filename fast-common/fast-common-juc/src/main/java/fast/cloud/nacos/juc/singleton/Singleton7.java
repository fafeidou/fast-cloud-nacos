package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public enum Singleton7 {
    INSTANCE;

    Singleton7() {
        System.out.println("Singleton7 实例化");
    }

    public static void method() {
        //调用该方法，会导致初始化
    }

    public static Singleton7 getInstance() {
        return INSTANCE;
    }
}
