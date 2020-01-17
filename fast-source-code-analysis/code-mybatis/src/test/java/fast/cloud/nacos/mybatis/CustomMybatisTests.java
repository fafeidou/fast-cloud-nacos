package fast.cloud.nacos.mybatis;

import fast.cloud.nacos.custom.mybatis.io.Resources;
import fast.cloud.nacos.custom.mybatis.session.SqlSession;
import fast.cloud.nacos.custom.mybatis.session.SqlSessionFactory;
import fast.cloud.nacos.custom.mybatis.session.SqlSessionFactoryBuilder;
import fast.cloud.nacos.mybatis.entity.DeptEmp;
import fast.cloud.nacos.mybatis.mapper.DeptEmpMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CustomMybatisTests {

    @Test
    public void testMybatis() throws IOException {
        //1. 加载核心配置文件
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");

        //2. 解析核心配置文件并创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);

        //3. 创建核心对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //4. 得到Mapper代理对象
        DeptEmpMapper deptEmpMapper = sqlSession.getMapper(DeptEmpMapper.class);

        //5. 调用自定义的方法实现查询功能
        List<DeptEmp> list = deptEmpMapper.getEmpTotalByDept();
        for (DeptEmp de : list) {
            System.out.println(de);
        }

        //6. 关闭sqlSession
        sqlSession.close();

    }



}
