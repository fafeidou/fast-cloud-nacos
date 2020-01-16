package fast.cloud.nacos.custom.mybatis.io;

import java.io.InputStream;

/**
 * 用于读取配置文件的类
 */
public class Resources {

    /**
     * 用于加载 xml 文件，并且得到一个流对象
     *
     * @param xmlPath
     * @return 在实际开发中读取配置文件:
     * 第一：使用类加载器。但是有要求：a 文件不能过大。 b 文件必须在类路径下(classpath)
     * 第二：使用 ServletContext 的 getRealPath()
     */
    public static InputStream getResourceAsStream(String xmlPath) {
        return Resources.class.getClassLoader().getResourceAsStream(xmlPath);
    }
}
