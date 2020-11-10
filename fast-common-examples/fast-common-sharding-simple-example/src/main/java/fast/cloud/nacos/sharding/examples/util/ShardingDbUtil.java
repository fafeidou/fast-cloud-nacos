package fast.cloud.nacos.sharding.examples.util;


/**
 * 分片工具类
 * <p>
 * 计算库和表的分片位置
 * <p>
 * 要先将shardingId做hash值计算，充分打乱，再取模。
 * 库和表的hash算法要不一样，防止一条记录落在相同的dbIndex和tableIndex。
 * <p>
 * hash值计算参考：https://www.jb51.net/article/124819.htm
 */
public class ShardingDbUtil {

    /**
     * 根据逻辑库名和index，返回实际的库名
     */
    public static String getActualDatabaseName(String logicalDatabaseName, Integer index) {
        return logicalDatabaseName + "_" + index;
    }

    /**
     * 根据用户信息和DB个数，计算实际库的编号
     */
    public static int getActualDatabaseIndex(Long shardingInfo, int dbNum) {
        return (RSHash(shardingInfo.toString())) % dbNum;
    }

    /**
     * 根据逻辑表名和index，返回实际的表名
     */
    public static String getActualTableName(String logicalTableName, Integer index) {
        return logicalTableName + "_" + index;
    }

    /**
     * 根据用户信息和DB个数，计算实际表的编号
     */
    public static int getActualTableIndex(Long shardingInfo, int tableNum) {
        return (intHash(shardingInfo.intValue())) % tableNum + 1;
    }

    public static int RSHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }
        return Math.abs(hash & 0x7FFFFFFF);
    }

    public static int intHash(int key) {
        key += ~(key << 15);
        key ^= (key >>> 10);
        key += (key << 3);
        key ^= (key >>> 6);
        key += ~(key << 11);
        key ^= (key >>> 16);
        return Math.abs(key);
    }

    /**
     * 拼接分片后的库和表的id，用于存储
     */
    public static String getShardingMeta(Long shardingInfo, int dbNum, int tableNum) {
        Integer dbIndex = getActualDatabaseIndex(shardingInfo, dbNum);
        Integer tableIndex = getActualTableIndex(shardingInfo, tableNum);
        return dbIndex.toString() + "," + tableIndex.toString();
    }
}
