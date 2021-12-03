package fast.cloud.nacos.metrics.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author qinfuxiang
 */
public class HelloAgent {

    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("jmxBean:name=hello");
        //create mbean and register mbean
        Hello hello = new Hello();
        hello.setName("123");
        server.registerMBean(hello, helloName);
        Thread.currentThread().join();
    }
}
