package fast.cloud.nacos.juc.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author qinfuxiang
 * @Date 2020/6/26 9:14
 */
public class MyClassLoaderTest {

    public static void main(String[] args)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MyClassloader myClassloader = new MyClassloader();
        Class<?> clazz = myClassloader.loadClass("fast.cloud.nacos.juc.classloader.HelloWorld");
        System.out.println(clazz.getClassLoader());
        //1
        Object helloWorld = clazz.newInstance();
        System.out.println(helloWorld);
        Method welcomeMethod = clazz.getMethod("welcome");
        Object result = welcomeMethod.invoke(helloWorld);
        System.out.println("Result: " + result);
    }
}
