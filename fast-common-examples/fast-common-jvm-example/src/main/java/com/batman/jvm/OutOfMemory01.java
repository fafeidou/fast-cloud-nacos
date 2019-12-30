package com.batman.jvm;

import java.util.ArrayList;
import java.util.List;

public class OutOfMemory01 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        while (true) {
            list.add(new String("test"));
        }
    }
}
// javac OutOfMemory01.java   | javac -d . OutOfMemory01.java 带有包名编译
//java -Xmx5m OutOfMemory01   | java -Xmx5m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc.hprof -XX:+PrintGCDetails -XX:+PrintGCDetails -Xloggc:./gc.log com.batman.jvm.OutOfMemory01

//-d .（点） ：代表的是把class 文件打到哪个目录里

//(base) batmandeMacBook-Pro:jvm batman$ java -Xmx5m com.batman.jvm.OutOfMemory01
//        Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
//        at java.util.Arrays.copyOf(Arrays.java:3210)
//        at java.util.Arrays.copyOf(Arrays.java:3181)
//        at java.util.ArrayList.grow(ArrayList.java:261)
//        at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:235)
//        at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:227)
//        at java.util.ArrayList.add(ArrayList.java:458)
//        at com.batman.jvm.OutOfMemory01.main(OutOfMemory01.java:12)


//java -Xmx5m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./gc.hprof -XX:+PrintGCDetails -XX:+PrintGCDetails -Xloggc:./gc.log com.batman.jvm.OutOfMemory01