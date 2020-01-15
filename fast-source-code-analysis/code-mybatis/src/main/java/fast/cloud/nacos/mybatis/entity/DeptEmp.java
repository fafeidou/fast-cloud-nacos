package fast.cloud.nacos.mybatis.entity;

//实体类
public class DeptEmp {

    private String dname;  //部门名
    private Integer total; //员工总数

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DeptEmp{" +
                "dname='" + dname + '\'' +
                ", total=" + total +
                '}';
    }
}
