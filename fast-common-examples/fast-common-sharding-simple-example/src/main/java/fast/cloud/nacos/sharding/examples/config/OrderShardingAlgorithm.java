package fast.cloud.nacos.sharding.examples.config;

import fast.cloud.nacos.sharding.examples.util.ShardingDbUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class OrderShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * 实际数据库/表的模板.
     */
    private static final String ACTUAL_TARGET_TEMPLATE = "%s_%d";
    private static final Long DATABASE_NUM = 1L;

    Integer TABLE_NUM = 2;

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        if (availableTargetNames.stream().findFirst().get().startsWith(shardingValue.getLogicTableName())) {
            // 表路由匹配
            int tableIndex = ShardingDbUtil.getActualTableIndex(shardingValue.getValue(), TABLE_NUM);
            String targetNode = String.format(ACTUAL_TARGET_TEMPLATE, shardingValue.getLogicTableName(), tableIndex);
            log.info("sharding table: {}", targetNode);
            return targetNode;
        } else {
            // 库路由匹配
            long databaseIndex = shardingValue.getValue() % DATABASE_NUM + 1;
            String targetNode = String
                    .format(ACTUAL_TARGET_TEMPLATE, "order_db", databaseIndex);
            log.info("sharding database: {}", targetNode);
            return targetNode;
        }
    }
}
