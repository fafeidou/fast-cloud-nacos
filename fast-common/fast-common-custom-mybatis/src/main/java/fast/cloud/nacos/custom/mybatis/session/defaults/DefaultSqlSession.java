package fast.cloud.nacos.custom.mybatis.session.defaults;

import fast.cloud.nacos.custom.mybatis.binding.MapperProxyFactory;
import fast.cloud.nacos.custom.mybatis.executor.Executor;
import fast.cloud.nacos.custom.mybatis.mapper.Mapper;
import fast.cloud.nacos.custom.mybatis.session.Configuration;
import fast.cloud.nacos.custom.mybatis.session.SqlSession;
import fast.cloud.nacos.custom.mybatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {
    //核心配置对象
    private Configuration cfg;

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    //连接对象
    private Connection conn;

    //调用 DataSourceUtils 工具类获取连接
    public Connection getConn() {

        try {
            conn = DataSourceUtil.getConnection(cfg);
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 动态代理：
     * 涉及的类：Proxy
     * 使用的方法：newProxyInstance
     * 方法的参数：
     * ClassLoader：和被代理对象使用相同的类加载器,通常都是固定的
     * Class[]：代理对象和被代理对象要求有相同的行为。（具有相同的方法）
     * InvocationHandler：如何代理。需要我们自己提供的增强部分的代码
     */
    @Override
    public <T> T getMapper(Class<T> daoClass) {
        conn = getConn();
        System.out.println(conn);
        T daoProxy = (T) Proxy.newProxyInstance(daoClass.getClassLoader(), new
                Class[]{daoClass}, new MapperProxyFactory(cfg.getMappers(), conn));
        return daoProxy;
    }

    //释放资源
    @Override
    public void close() {
        try {
            System.out.println(conn);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询所有方法
    public <E> List<E> selectList(String statement) {
        Mapper mapper = cfg.getMappers().get(statement);
        return new Executor().selectList(mapper, conn);
    }
}