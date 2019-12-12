package fast.cloud.nacos.securityauth.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FooUser {
    private String id;
    private String username;
    private String password;
    private String salt;
    private String name;
    private String utype;
    private String birthday;
    private String userpic;
    private String sex;
    private String email;
    private String phone;
    private String status;
    private Date createTime;
    private Date updateTime;
}
