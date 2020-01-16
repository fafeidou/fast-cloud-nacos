package fast.cloud.nacos.custom.mybatis.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 *  用于封装查询时的必要信息：要执行的 SQL 语句和实体类的全限定类名
 */
public class Mapper {
    private String queryString;//sql
    private String resultType;//结果类型的全限定类名
    private Map<String,String> propMapper = new HashMap<>(); //普通的属性和字段信息

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Map<String, String> getPropMapper() {
        return propMapper;
    }

    public void setPropMapper(Map<String, String> propMapper) {
        this.propMapper = propMapper;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "queryString='" + queryString + '\'' +
                ", resultType='" + resultType + '\'' +
                ", propMapper=" + propMapper +
                '}';
    }
}
