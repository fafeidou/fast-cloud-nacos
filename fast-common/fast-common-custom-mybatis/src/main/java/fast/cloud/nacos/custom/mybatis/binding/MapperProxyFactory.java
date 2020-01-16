package fast.cloud.nacos.custom.mybatis.binding;

import fast.cloud.nacos.custom.mybatis.executor.Executor;
import fast.cloud.nacos.custom.mybatis.mapper.Mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class MapperProxyFactory implements InvocationHandler {
    private Map<String, Mapper> mappers;
    private Connection conn;

    public MapperProxyFactory(Map<String, Mapper> mappers, Connection conn) {
        this.mappers = mappers;
        this.conn = conn;
    }

    /**
     * 对当前正在执行的方法进行增强
     * 取出当前执行的方法名称
     * 取出当前执行的方法所在类
     * 拼接成 key
     * 去 Map 中获取 Value（Mapper)
     * 使用工具类 Executor 的 selectList 方法x
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.取出方法名
        String methodName = method.getName();
        //2.取出方法所在类名
        String className = method.getDeclaringClass().getName();
        //3.拼接成 Key
        String key = className + "." + methodName;
        //4.使用 key 取出 mapper
        Mapper mapper = mappers.get(key);
        if (mapper == null) {
            throw new IllegalArgumentException("传入的参数有误，无法获取执行的必要条件 ");
        }
        //5.创建 Executor 对象
        Executor executor = new Executor();
        return executor.selectList(mapper, conn);
    }
}