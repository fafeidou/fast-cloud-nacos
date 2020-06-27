package fast.cloud.nacos.juc.classloader;

/**
 * @author qinfuxiang
 * @Date 2020/6/27 13:49
 */
public class NameSpace {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = NameSpace.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        Class<?> bClass = classLoader.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        System.out.println(aClass.hashCode());
        System.out.println(bClass.hashCode());
        System.out.println(aClass == bClass);
    }
}
