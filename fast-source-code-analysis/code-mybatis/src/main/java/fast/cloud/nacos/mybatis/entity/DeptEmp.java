package fast.cloud.nacos.mybatis.entity;

//实体类
public class DeptEmp {

    private Integer deptno;  //部门名
    private Long total; //员工总数

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    @Override
    public String toString() {
        return "DeptEmp{" +
                "deptno=" + deptno +
                ", total=" + total +
                '}';
    }
}
