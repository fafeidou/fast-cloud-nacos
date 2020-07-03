package fast.cloud.nacos.juc.threadlocal;

/**
 * @author qinfuxiang
 * @Date 2020/7/2 20:13
 */
public class InheritableThreadLocalParentChild {

    public static void main(String[] args) {

        final InheritableThreadLocal threadLocal = new InheritableThreadLocal() {
            @Override
            protected Object initialValue() {
                return "abc";
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(threadLocal.get());
            }
        }).start();
    }

}
