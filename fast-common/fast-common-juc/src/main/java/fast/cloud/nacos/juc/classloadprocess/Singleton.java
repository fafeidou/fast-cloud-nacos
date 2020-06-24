package fast.cloud.nacos.juc.classloadprocess;

/**
 * @author qinfuxiang
 */
public class Singleton {
    //1
    private static int x = 0;

    private static int y;

    private static Singleton instance = new Singleton(); //2

    private Singleton() {
        x++;
        y++;
    }

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        System.out.println(Singleton.x);
        System.out.println(Singleton.y);

    }


}
