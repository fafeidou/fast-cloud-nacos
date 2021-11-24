package fast.boot.autoconfigure.postprocessor;

import javax.annotation.PostConstruct;

public class User {

    private int id;

    private String name;

    private String beanName;

    public User(){
        System.out.println("User 被实例化");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("设置："+name);
        this.name = name;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @PostConstruct
    public void start(){
        System.out.println("自定义初始化的方法....");
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", beanName=" + beanName + "]";
    }
}
