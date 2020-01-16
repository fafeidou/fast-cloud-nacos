package fast.cloud.nacos.custom.mybatis.session;

/**
 *  操作数据库的核心对象
 */
public interface SqlSession {
    /**
     * 创建 Dao 接口的代理对象
     * @param daoClass
     * @return
     */
    <T> T getMapper(Class<T> daoClass);
    /**
     * 释放资源
     */
    void close();
}
