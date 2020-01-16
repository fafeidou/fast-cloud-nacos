package fast.cloud.nacos.custom.mybatis.mapper;

/**
 *  用于封装查询时的必要信息：要执行的 SQL 语句和实体类的全限定类名
 */
public class Mapper {
    private String queryString;//sql
    private String resultType;//结果类型的全限定类名

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
}
