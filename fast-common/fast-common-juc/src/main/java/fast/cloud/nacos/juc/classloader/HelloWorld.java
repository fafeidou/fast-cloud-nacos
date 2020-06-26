package fast.cloud.nacos.juc.classloader;

/**
 * @author qinfuxiang
 * @Date 2020/6/26 9:12
 */
public class HelloWorld {
    static {
        System.out.println("HelloWorld class is init");
    }

    public String welcome() {
        return "hello world";
    }

    public static void main(String[] args) {
        new HelloWorld();
    }
}
