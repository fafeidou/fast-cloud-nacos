package fast.cloud.nacos.juc.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author qinfuxiang
 * @Date 2020/6/25 22:54
 */
public class MyClassloader extends ClassLoader {

    private final static Path DEFAULT_CLASS_DIR = Paths.get("C:", "classload1");

    private final Path classDir;

    public MyClassloader() {
        this.classDir = DEFAULT_CLASS_DIR;
    }

    public MyClassloader(String classDir) {
        super();
        this.classDir = Paths.get(classDir);
    }

    public MyClassloader(String classDir, ClassLoader parent) {
        super(parent);
        this.classDir = Paths.get(classDir);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        //设置class的二进制数据
        byte[] classBytes = this.readClassBytes(name);
        if (null == classBytes || classBytes.length == 0) {
            throw new ClassNotFoundException("can not load this class" + name);
        }
        //调用defineClass方法定义class
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] readClassBytes(String name) throws ClassNotFoundException {
        String classPath = name.replace(".", "/");
        Path classFullPath = classDir.resolve(Paths.get(classPath + ".class"));
        if (!classFullPath.toFile().exists()) {
            throw new ClassNotFoundException("not fount class:" + name);
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Files.copy(classFullPath, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException("this class" + name + "error:" + e);
        }
    }

    @Override
    public String toString() {
        return "MyClass Loader";
    }
}
