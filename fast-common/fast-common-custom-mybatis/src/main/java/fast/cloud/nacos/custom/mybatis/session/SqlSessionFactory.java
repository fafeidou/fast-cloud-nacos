package fast.cloud.nacos.custom.mybatis.session;

/**
 * SqlSessionFactory 的接口
 */
public interface SqlSessionFactory {
    /**
     * 创建一个新的 SqlSession 对象
     */
    SqlSession openSession();
}
