@[toc]

#  前言

单例模式相对来说，设计比较简单，但是实现方式多种多样，我们需要从线程安全、高性能、懒加载方面进行评估。


# 饿汉式

实例代码如下：

```java
public final class Singleton1 {

    private byte[] data = new byte[1024];

    private static Singleton1 instance = new Singleton1();

    private Singleton1() {
        System.out.println("Singleton1 实例化");
    }

    public static Singleton1 getInstance() {
        return instance;
    }
}

```

根据前面可以知道，在类的初始化阶段类变量进行初始化，就是instance变量会被初始化，同时呢1k的空间data也被创建，我们可以写段代码测试下：

```java
public class Test {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass1 = Class.forName("fast.cloud.nacos.juc.singleton.Singleton1");
    }
}

```

运行代码会输出：

```
Singleton1 实例化
```

这也说明了`Class.forName`方法会对类进行初始化

>  关于这点不懂得可以看看前面的博客，有解释到的，Class.forName 和 ClassLoader的区别

如果一个类属性较少，占用内存也较小，用饿汉式也未尝不可。

总而言之，饿汉式不支持懒加载，可以保证多线程下只被加载一次，性能也比较高。

# 懒汉式

所谓懒汉式就是当你使用的时候再去创建，代码如下

```java

public final class Singleton2 {
    private byte[] data = new byte[1024];

    private static Singleton2 instance = null;

    private Singleton2() {
        System.out.println("Singleton2 实例化");
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}

```

这种方式会有一个问题，当两个线程都走在 `instance == null`时，这个时候会创建两个实例，线程不安全。


# 懒汉式 + 同步方法

代码如下

```java

public final class Singleton3 {

    private byte[] data = new byte[1024];

    private static Singleton3 instance = null;

    private Singleton3() {
        System.out.println("Singleton3 实例化");
    }

    //加锁，每次只有一个线程获得
    public static synchronized Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}

```

这种方式的问题在于，`synchronized`的排他性，同一时刻，只能被一个线程访问，性能低下。


# Double-Check


这种方法就是，在初次初始化的时候进行加锁，之后就需要加锁了，提高了效率。

```java

public final class Singleton4 {
    private byte[] data = new byte[1024];

    private static Singleton4 instance = null;

    private Singleton4() {
        System.out.println("Singleton4 实例化");
    }

    public static  Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}

```

这种会出现空指针异常，我们来逐步分析下：

主要在于singleton = new Singleton4()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情。
　　1. 给 singleton 分配内存
　　2. 调用 Singleton 的构造函数来初始化成员变量，形成实例
　　3. 将singleton对象指向分配的内存空间（执行完这步 singleton才是非 null了）

在JVM的即时编译器中存在指令重排序的优化。


也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，
被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

解决方案就是加上`volatile`，就可以了


```java

public final class Singleton5 {

    private byte[] data = new byte[1024];

    /**
     * static 和 volatile 切换，编译器不会报错
     */
    private static volatile Singleton5 instance = null;

    private Singleton5() {
        System.out.println("Singleton5 实例化");
    }

    public static Singleton5 getInstance() {
        if (instance == null) {
            synchronized (Singleton5.class) {
                if (instance == null) {
                    instance = new Singleton5();
                }
            }
        }
        return instance;
    }
}

```

> 这了需要注意
> volatile阻止的不是singleton = new Singleton()这句话内部[1-2-3]的指令重排，而是保证了在一个写操作（[1-2-3]）完成之前，
>不会调用读操作（if (instance == null)）。


#  Holder方式

Holder方式借住类加载的特点，同一类加载器只会对同一个类加载一次（这个时候可能被问到类加载器底层如何保证只被加载一次，可以翻翻我前面的博客）

```java

public final class Singleton6 {

    private byte[] data = new byte[1024];


    private static final class Holder {
        private static Singleton6 instance = new Singleton6();
    }

    public static Singleton6 getInstance() {
        return Holder.instance;
    }
}


```

`Holder`会在编译的时候被收集到`<clinit>`方法中，该方法可以保证线程安全。


`Singleton6`也可以保证懒加载，只有在初次调用`getInstance`方法时，才会初始化`Holder`中的实例


# 枚举方式

利用自动序列化机制，保证了线程的绝对安全

```java

public enum Singleton7 {
    INSTANCE;

    Singleton7() {
        System.out.println("Singleton7 实例化");
    }

    public static void method() {
        //调用该方法，会导致初始化
    }

    public static Singleton7 getInstance() {
        return INSTANCE;
    }
}

```

这种方法如果调用了外部调用了静态方法也会初始化，可以加上我们的`Holder`模式

# 枚举-Holder

```java

public class Singleton8 {

    private byte[] data = new byte[1024];

    private Singleton8() {

    }
    public static void method() {
        //调用该方法，不会导致初始化
    }

    private enum EnumHolder{
        INSTANCE;

        private  Singleton8 instance;
        private EnumHolder() {
            System.out.println("Singleton8 实例化");
            this.instance = new Singleton8();
        }

        public Singleton8 getInstance() {
            return instance;
        }
    }

    public Singleton8 getInstance() {
        return EnumHolder.INSTANCE.getInstance();
    }


}

```

#  总结

开发过程中，个人用的比较多的也是`Holder`模式和枚举模式吧。
