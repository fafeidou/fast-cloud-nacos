package fast.cloud.nacos.custom.mybatis.utils;

import fast.cloud.nacos.custom.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataSourceUtil {
    /**
     * 获取连接
     * @param cfg
     * @return
     */
    public static Connection getConnection(Configuration cfg) {
        try {
            Class.forName(cfg.getDriver());
            Connection conn =
                    DriverManager.getConnection(cfg.getUrl(),cfg.getUsername() , cfg.getPassword());
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
