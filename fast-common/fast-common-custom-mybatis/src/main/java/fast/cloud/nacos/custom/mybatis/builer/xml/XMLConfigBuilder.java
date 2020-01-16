package fast.cloud.nacos.custom.mybatis.builer.xml;

import fast.cloud.nacos.custom.mybatis.annotation.Select;
import fast.cloud.nacos.custom.mybatis.io.Resources;
import fast.cloud.nacos.custom.mybatis.mapper.Mapper;
import fast.cloud.nacos.custom.mybatis.session.Configuration;
import fast.cloud.nacos.custom.mybatis.session.defaults.DefaultSqlSession;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于解析配置文件
 */
public class XMLConfigBuilder {
    /**
     * 解析主配置文件，把里面的内容填充到 DefaultSqlSession 所需要的地方
     * 使用的技术：
     * dom4j+xpath
     *
     * @param session
     */

    public static void loadConfiguration(DefaultSqlSession session, InputStream
            config) {
        try {
            //定义封装连接信息的配置对象（mybatis 的配置对象）
            Configuration cfg = new Configuration();
            //1.获取 SAXReader 对象
            SAXReader reader = new SAXReader();
            //2.根据字节输入流获取 Document 对象
            Document document = reader.read(config);
            //3.获取根节点
            Element root = document.getRootElement();
            //4.使用 xpath 中选择指定节点的方式，获取所有 property 节点
            List<Element> propertyElements = root.selectNodes("//property");
            //5.遍历节点
            for (Element propertyElement : propertyElements) {
                //判断节点是连接数据库的哪部分信息
                //取出 name 属性的值
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)) {
                    //表示驱动
                    //获取 property 标签 value 属性的值
                    String driver = propertyElement.attributeValue("value");
                    cfg.setDriver(driver);
                }
                if ("url".equals(name)) {
                    //表示连接字符串
                    //获取 property 标签 value 属性的值
                    String url = propertyElement.attributeValue("value");
                    cfg.setUrl(url);
                }
                if ("username".equals(name)) {
                    //表示用户名
                    //获取 property 标签 value 属性的值
                    String username = propertyElement.attributeValue("value");
                    cfg.setUsername(username);
                }
                if ("password".equals(name)) {
                    //表示密码
                    //获取 property 标签 value 属性的值
                    String password = propertyElement.attributeValue("value");
                    cfg.setPassword(password);
                }
            }
            //取出 mappers 中的所有 mapper 标签，判断他们使用了 resource 还是 class 属性
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            //遍历集合
            for (Element mapperElement : mapperElements) {
                //判断 mapperElement 使用的是哪个属性
                Attribute attribute = mapperElement.attribute("resource");
                if (attribute != null) {
                    System.out.println("使用的是 XML");
                    //表示有 resource 属性，用的是 XML
                    //取出属性的值
                    String mapperPath = attribute.getValue();// 获 取 属 性 的 值 "com/dao/IUserDao.xml"
                    //把映射配置文件的内容获取出来，封装成一个 map
                    Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
                    //给 configuration 中的 mappers 赋值
                    cfg.setMappers(mappers);
                } else {
                    System.out.println("使用的是注解");
                    //表示没有 resource 属性，用的是注解
                    //获取 class 属性的值
                    String daoClassPath = mapperElement.attributeValue("class");
                    //根据 daoClassPath 获取封装的必要信息
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    //给 configuration 中的 mappers 赋值
                    cfg.setMappers(mappers);
                }
            }
            //把配置对象传递给 DefaultSqlSession
            session.setCfg(cfg);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                config.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据传入的参数，解析 XML，并且封装到 Map 中
     *
     * @param mapperPath 映射配置文件的位置
     * @return map 中包含了获取的唯一标识（key 是由 dao 的全限定类名和方法名组成）
     * 以及执行所需的必要信息（value 是一个 Mapper 对象，里面存放的是执行的 SQL 语句和
     * 要封装的实体类全限定类名）
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) throws IOException {
        InputStream in = null;
        try {
            //定义返回值对象
            Map<String, Mapper> mappers = new HashMap<String, Mapper>();
            //1.根据路径获取字节输入流
            in = Resources.getResourceAsStream(mapperPath);
            //2.根据字节输入流获取 Document 对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            //3.获取根节点
            Element root = document.getRootElement();
            //4.获取根节点的 namespace 属性取值
            String namespace = root.attributeValue("namespace");//是组成 map 中 key 的部分
            //5.获取所有的 select 节点
            List<Element> selectElements = root.selectNodes("//select");
            //6.遍历 select 节点集合
            for (Element selectElement : selectElements) {
                //取出 id 属性的值 组成 map 中 key 的部分
                String id = selectElement.attributeValue("id");
                //取出 resultType 属性的值 组成 map 中 value 的部分
                String resultType = selectElement.attributeValue("resultType");
                //取出文本内容 组成 map 中 value 的部分
                String queryString = selectElement.getText();
                //创建 Key
                String key = namespace + "." + id;
                //创建 Value
                Mapper mapper = new Mapper();
                mapper.setQueryString(queryString);
                mapper.setResultType(resultType);
                //把 key 和 value 存入 mappers 中
                mappers.put(key, mapper);
            }
            return mappers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            in.close();
        }
    }

    /**
     * 根据传入的参数，得到 dao 中所有被 select 注解标注的方法。
     * 根据方法名称和类名，以及方法上注解 value 属性的值，组成 Mapper 的必要信息
     *
     * @param daoClassPath
     * @return
     */
    private static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) throws Exception {
        //定义返回值对象
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        //1.得到 dao 接口的字节码对象
        Class daoClass = Class.forName(daoClassPath);
        //2.得到 dao 接口中的方法数组
        Method[] methods = daoClass.getMethods();
        //3.遍历 Method 数组
        for (Method method : methods) {
            //取出每一个方法，判断是否有 select 注解
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                //创建 Mapper 对象
                Mapper mapper = new Mapper();
                //取出注解的 value 属性值
                Select selectAnno = method.getAnnotation(Select.class);
                String queryString = selectAnno.value();
                mapper.setQueryString(queryString);
                //获取当前方法的返回值，还要求必须带有泛型信息
                Type type = method.getGenericReturnType();//List<User>
                //判断 type 是不是参数化的类型
                if (type instanceof ParameterizedType) {
                    //强转
                    ParameterizedType ptype = (ParameterizedType) type;
                    //得到参数化类型中的实际类型参数
                    Type[] types = ptype.getActualTypeArguments();
                    //取出第一个
                    Class domainClass = (Class) types[0];
                    //获取 domainClass 的类名
                    String resultType = domainClass.getName();
                    //给 Mapper 赋值
                    mapper.setResultType(resultType);
                }
                //组装 key 的信息
                //获取方法的名称
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + "." + methodName;
                //给 map 赋值
                mappers.put(key, mapper);
            }
        }
        return mappers;
    }
}


