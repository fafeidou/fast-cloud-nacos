package fast.cloud.nacos.juc.singleton;

/**
 * @author qinfuxiang
 */
public class Test {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass1 = Class.forName("fast.cloud.nacos.juc.singleton.Singleton1");
        Class<?> aClass2 = Class.forName("fast.cloud.nacos.juc.singleton.Singleton2");
        Singleton7.method();
        Singleton8.method();
    }
}
