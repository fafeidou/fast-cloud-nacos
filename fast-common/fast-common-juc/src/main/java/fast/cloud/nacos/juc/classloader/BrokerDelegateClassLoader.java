package fast.cloud.nacos.juc.classloader;

/**
 * @author qinfuxiang
 * @Date 2020/6/27 9:36
 */
public class BrokerDelegateClassLoader extends MyClassloader {

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //1
        synchronized (getClassLoadingLock(name)) {
            //2
            Class<?> clazz = findLoadedClass(name);
            //3
            if (clazz == null) {
                //4
                if (name.startsWith("java.") || name.startsWith("javax")) {
                    try {
                        clazz = getSystemClassLoader().loadClass(name);
                    } catch (Exception e) {
                        //ignore
                    }
                } else {
                    //5
                    try {
                        clazz = this.findClass(name);
                    } catch (ClassNotFoundException e) {
                        //ignore
                    }
                    //6
                    if (clazz == null) {
                        if (getParent() != null) {
                            clazz = getParent().loadClass(name);
                        } else {
                            clazz = getSystemClassLoader().loadClass(name);
                        }
                    }
                }
            }
            if (null == clazz) {
                throw new ClassNotFoundException("The class " + name + " not found.");
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }
}
