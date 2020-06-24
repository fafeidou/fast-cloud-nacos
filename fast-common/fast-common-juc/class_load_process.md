 类的加载过程

#  类的加载过程及简介

类的加载过程，可分为三个阶段，加载、连接、初始化。

* 加载阶段，加载类的二进制文件，其实就是class文件

* 连接又可以分为三个：
  * 验证，保证类的正确性，class的版本，class文件的魔术因子是否正确。
  * 准备，为类的静态变量分配内存，并且为其初始化默认值。
  * 解析，把类中的符号引用转换为直接引用。

* 初始化，为类的静态变量赋予默认的初始值

# 类的主动调用

JVM规范规定了以下六种主动使用类的场景

* 通过new关键字初始化
* 访问类的静态变量
* 访问类的静态方法
* 对某个类进行反射操作
* 初始化子类会导致父类的初始化
* 执行main函数所在的类

# 类的被动调用

被动调用，不会导致类的加载和初始化

* 构造某个类的数组时，不会导致该类的初始化（不要因为new关键字误导，事实上该操作只不过是在堆内存上开辟的连续内存空间）

* 引用类的静态常量不会导致类的初始化


# 类的加载过程详解


先看一个面试题

```java

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

```

程序的输出结果是什么呢，如果1和2的位置切换，结果又是什么呢？



