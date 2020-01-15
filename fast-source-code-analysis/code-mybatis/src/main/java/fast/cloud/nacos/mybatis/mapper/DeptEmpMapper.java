package fast.cloud.nacos.mybatis.mapper;

import fast.cloud.nacos.mybatis.entity.DeptEmp;

import java.util.List;

//Mapper接口  不用编写实现类
public interface DeptEmpMapper {

    List<DeptEmp> getEmpTotalByDept();

}
