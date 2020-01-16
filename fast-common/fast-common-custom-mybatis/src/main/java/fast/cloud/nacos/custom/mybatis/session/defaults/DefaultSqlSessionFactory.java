package fast.cloud.nacos.custom.mybatis.session.defaults;

import fast.cloud.nacos.custom.mybatis.builer.xml.XMLConfigBuilder;
import fast.cloud.nacos.custom.mybatis.session.SqlSession;
import fast.cloud.nacos.custom.mybatis.session.SqlSessionFactory;

import java.io.InputStream;

/**
 * SqlSessionFactory 的默认实现
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private InputStream config = null;

    public void setConfig(InputStream config) {
        this.config = config;
    }

    @Override
    public SqlSession openSession() {
        DefaultSqlSession session = new DefaultSqlSession();
        //调用工具类解析 xml 文件
        XMLConfigBuilder.loadConfiguration(session, config);
        return session;
    }
}