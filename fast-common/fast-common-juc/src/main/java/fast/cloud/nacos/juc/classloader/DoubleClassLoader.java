package fast.cloud.nacos.juc.classloader;

/**
 * @author qinfuxiang
 * @Date 2020/6/27 14:03
 */
public class DoubleClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        BrokerDelegateClassLoader myClassloaderA = new BrokerDelegateClassLoader();
        BrokerDelegateClassLoader myClassloaderB = new BrokerDelegateClassLoader();
        Class<?> clazzA = myClassloaderA.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        Class<?> clazzB = myClassloaderB.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        System.out.println(clazzA.getClassLoader());
        System.out.println(clazzB.getClassLoader());
        System.out.println(clazzA.hashCode());
        System.out.println(clazzB.hashCode());
        System.out.println(clazzA == clazzB);
    }

}
