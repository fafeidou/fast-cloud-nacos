package fast.cloud.nacos.sharding.examples.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Configuration
@Slf4j
public class ShardingJdbcConfig {
//    @Autowired
    private OrderShardingAlgorithm orderShardingAlgorithm;

    //配置分片规则
    // 定义数据源
    Map<String, DataSource> createDataSourceMap() {
        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setDbType("com.alibaba.druid.pool.DruidDataSource");
        dataSource1.setUrl("jdbc:mysql://192.168.56.121:3306/order_db?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");
        dataSource1.setMaxActive(100);
        dataSource1.setInitialSize(5);
        Map<String, DataSource> result = new HashMap<>();
        result.put("m1", dataSource1);
        return result;
    }

    // 定义主键生成策略
    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "order_id");
        return result;
    }

    // 定义t_order表的分片策略
    TableRuleConfiguration getOrderInlineTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order", "m1.t_order_$->{1..2}");
        result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_$->{order_id % 2 + 1}"));
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());

        return result;
    }

    TableRuleConfiguration getOrderStandardTableRuleConfiguration() {
        return getStandardTableRuleConfiguration("t_order",
                "order_id", orderShardingAlgorithm);
    }

    // 定义sharding-Jdbc数据源
    @Bean
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderInlineTableRuleConfiguration());
        //spring.shardingsphere.props.sql.show = true
        Properties properties = new Properties();
        properties.put("sql.show", "true");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, properties);
    }

    /**
     * 获取标准分片表配置：只能有一个分片键.
     *
     * @param logicalTableName  逻辑表名
     * @param shardingColumn    分片字段
     * @param shardingAlgorithm 分片算法
     */
    private TableRuleConfiguration getStandardTableRuleConfiguration(String logicalTableName, String shardingColumn,
                                                                     PreciseShardingAlgorithm shardingAlgorithm) {
        String dataNodeList = getDoDataNodes("order_db", 1,
                logicalTableName, 2);
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration(logicalTableName, dataNodeList);
        // 创建分片策略
        StandardShardingStrategyConfiguration strategyConfiguration = new StandardShardingStrategyConfiguration(
                shardingColumn, shardingAlgorithm);
        // 设置表分片策略
        tableRuleConfiguration.setTableShardingStrategyConfig(strategyConfiguration);
        return tableRuleConfiguration;
    }

    /**
     * 获取数据结点表达式：sharding-sphere 规则，不要随便修改
     *
     * @param databaseName 逻辑库名
     * @param databaseNum  数据库个数
     * @param tableName    逻辑表名
     * @param tableNum     每个库的表个数
     */
    private String getDoDataNodes(String databaseName, int databaseNum, String tableName, int tableNum) {
        String dataNodes;
        if (tableNum > 0) {
            dataNodes = String
                    .format("%s_${0..%d}.%s_${0..%d}", databaseName, databaseNum - 1, tableName, tableNum - 1);
        } else {
            dataNodes = String.format("%s_${0..%d}.%s", databaseName, databaseNum - 1, tableName);
        }
        log.info("getDoDataNodes dbName={}, dbNum={}, tableName={}, tableNum={}, dataNodes={}",
                databaseName, databaseNum, tableName, tableNum, dataNodes);
        return dataNodes;
    }
}
