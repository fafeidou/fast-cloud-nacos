package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public final class Singleton6 {

    private byte[] data = new byte[1024];


    private static final class Holder {
        private static Singleton6 instance = new Singleton6();
    }

    public static Singleton6 getInstance() {
        return Holder.instance;
    }
}
