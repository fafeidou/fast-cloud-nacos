package fast.cloud.nacos.mybatis.entity;

import fast.cloud.nacos.custom.mybatis.annotation.ORMColumn;

//实体类
public class DeptEmp {
    @ORMColumn(name = "deptno")
    private Integer dept;  //部门名
    private Long total; //员工总数

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long  total) {
        this.total = total;
    }

    public Integer getDept() {
        return dept;
    }

    public void setDept(Integer dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "DeptEmp{" +
                "dept=" + dept +
                ", total=" + total +
                '}';
    }
}
