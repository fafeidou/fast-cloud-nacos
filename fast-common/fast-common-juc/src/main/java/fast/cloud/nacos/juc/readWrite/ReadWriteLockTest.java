package fast.cloud.nacos.juc.readWrite;

/**
 * @author qinfuxiang
 * @Date 2020/6/22 16:24
 */
public class ReadWriteLockTest {

    public static String text = "ReadWriteLockTestReadWriteLockTest";

    public static void main(String[] args) {
        final ShareData shareData = new ShareData(50);
                for (int i = 0; i < 2; i++) {

                    new Thread(() -> {
                        for (int j = 0; j < text.length(); j++) {
                            try {
                                shareData.write(text.charAt(j));
                                System.out.println(Thread.currentThread() + " write " + text.charAt(j));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

        for (int i = 0; i < 10; i++) {
            while (true) {
                new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread() + " read " + String.valueOf(shareData.read()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

}
