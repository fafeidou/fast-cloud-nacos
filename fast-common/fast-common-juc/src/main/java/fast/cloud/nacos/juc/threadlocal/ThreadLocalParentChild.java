package fast.cloud.nacos.juc.threadlocal;

/**
 * @author qinfuxiang
 * @Date 2020/7/2 20:09
 */
public class ThreadLocalParentChild {

    public static void main(String[] args) {

        final ThreadLocal threadLocal = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return "abc";
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(threadLocal.get());//NULL
            }
        }).start();
    }

}
