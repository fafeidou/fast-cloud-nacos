package fast.cloud.nacos.custom.mybatis.session;

import fast.cloud.nacos.custom.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    /**
     * 根据传入的流，实现对 SqlSessionFactory 的创建
     *
     * @param in 它就是 SqlMapConfig.xml 的配置以及里面包含的 IUserDao.xml 的配置
     * @return
     */
    public SqlSessionFactory build(InputStream in) {
        DefaultSqlSessionFactory factory = new DefaultSqlSessionFactory();
        //给 factory 中 config 赋值
        factory.setConfig(in);
        return factory;
    }
}
