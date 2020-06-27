package fast.cloud.nacos.juc.classloader;

/**
 * @author qinfuxiang
 * @Date 2020/6/27 13:58
 */
public class DiffClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        MyClassloader myClassloader = new MyClassloader();
        BrokerDelegateClassLoader brokerDelegateClassLoader = new BrokerDelegateClassLoader();
        Class<?> clazzA = myClassloader.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        Class<?> clazzB = brokerDelegateClassLoader.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        System.out.println(clazzA.getClassLoader());
        System.out.println(clazzB.getClassLoader());
        System.out.println(clazzA.hashCode());
        System.out.println(clazzB.hashCode());
        System.out.println(clazzA == clazzB);

    }
}
