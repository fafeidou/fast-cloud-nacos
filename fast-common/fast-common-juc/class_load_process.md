 

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


```java
    //1
    private static int x = 0;

    private static int y;

    private static Singleton instance = new Singleton(); //2
```

在连接阶段准备过程中，每个变量都被赋予了相应的初始值

x=0 , y=0 , instance = null

下面跳过解析过程，再来看类的初始化阶段，初始化阶段会为每一个类变量赋值

x = 0 , y = 0 , instance = new Singleton()

在net Singleton()的时候，会执行构造函数，x和y会自增，因此结果为：

x = 1, y = 1

再来看调换顺序之后的输出


```java
    private static Singleton instance = new Singleton(); //2
    //1
    private static int x = 0;

    private static int y;

```

在连接阶段准备过程中，每个变量都被赋予了相应的初始值

instance = null, x=0 , y=0 

在类的初始化阶段，需要为每一个类赋予程序编写时所期待的正确的初始值，首先会进入instance的构造函数中


instance = new Singleton(), x=1, y=1

然后，为x初始化，由于x没有显式的赋值，因此0才是所期望的值，而y没有给定初始值，在构造函数中计算所得的值就是正确赋值，因此结果
会变成：

instance = new Singleton(), x=0, y=1



