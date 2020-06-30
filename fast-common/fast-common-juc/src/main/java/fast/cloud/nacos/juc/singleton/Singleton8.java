package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public class Singleton8 {

    private byte[] data = new byte[1024];

    private Singleton8() {

    }
    public static void method() {
        //调用该方法，不会导致初始化
    }

    private enum EnumHolder{
        INSTANCE;

        private  Singleton8 instance;
        private EnumHolder() {
            System.out.println("Singleton8 实例化");
            this.instance = new Singleton8();
        }

        public Singleton8 getInstance() {
            return instance;
        }
    }

    public Singleton8 getInstance() {
        return EnumHolder.INSTANCE.getInstance();
    }


}
