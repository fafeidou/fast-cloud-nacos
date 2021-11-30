package fast.cloud.nacos.codejuc.jmh;


import joptsimple.internal.Strings;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayListVsLinkedList {
    private final static String DATA_STRING = "DUMMY DATA";
    private final static int MAX_CAPACITY = 1_000_000;
    private final static int MAX_ITERATIONS = 1000;

    private static void test(List<String> list) {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            list.add(DATA_STRING);
        }
    }

    private static void arrayListPerfTest(int iterations) {
        final List<String> list = new ArrayList<>();
        final StopWatch stopWatch = new StopWatch("arrayList");
        stopWatch.start();
        test(list);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    private static void linkListPerfTest(int iterations) {
        final List<String> list = new LinkedList<>();
        final StopWatch stopWatch = new StopWatch("linkedList");
        stopWatch.start();
        test(list);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    public static void main(String[] args) {
        arrayListPerfTest(MAX_ITERATIONS);
        System.out.println(Strings.repeat('#',100));
        linkListPerfTest(MAX_ITERATIONS);
    }
}
