package fast.cloud.nacos.custom.mybatis.executor;

import fast.cloud.nacos.custom.mybatis.mapper.Mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 负责执行 SQL 语句，并且封装结果集
 */
public class Executor {
    public <E> List<E> selectList(Mapper mapper, Map<String, String> typeAliasesMap, Connection conn) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            //1.取出 mapper 中的数据
            String queryString = mapper.getQueryString();//select * from user
            String resultType = mapper.getResultType();//com.domain.User
            Class domainClass = Class.forName(resultType);//User.class
            //2.获取 PreparedStatement 对象
            pstm = conn.prepareStatement(queryString);
            //3.执行 SQL 语句，获取结果集
            rs = pstm.executeQuery();
            //4.封装结果集
            List<E> list = new ArrayList<E>();//定义返回值
            while (rs.next()) {
                //实例化要封装的实体类对象
                E obj = (E) domainClass.newInstance();//User 对象
                //取出结果集的元信息：ResultSetMetaData
                ResultSetMetaData rsmd = rs.getMetaData();
                //取出总列数
                int columnCount = rsmd.getColumnCount();
                //遍历总列数
                for (int i = 1; i <= columnCount; i++) {
                    //获取每列的名称，列名的序号是从 1 开始的
                    String columnName = rsmd.getColumnName(i);
                    //根据得到列名，获取每列的值
                    Object columnValue = rs.getObject(columnName);
                    String proName = typeAliasesMap.getOrDefault(columnName, columnName);
                    //给 obj 赋值：使用 Java 内省机制（借助 PropertyDescriptor 实现属性的封装）
                    PropertyDescriptor pd = new
                            PropertyDescriptor(proName, domainClass);//要求：实体类的属性和数据库表的列名保持一种
                    //获取它的写入方法
                    Method writeMethod = pd.getWriteMethod();//setUsername(String username);
                    //把获取的列的值，给对象赋值
                    writeMethod.invoke(obj, columnValue);
                }
                //把赋好值的对象加入到集合中
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(pstm, rs);
        }
    }

    private void release(PreparedStatement pstm, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pstm != null) {
            try {
                pstm.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
