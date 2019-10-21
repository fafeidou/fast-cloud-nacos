package fast.cloud.nacos.example.web.bean;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class User {
    @Size(min = 1, max = 10, message = "姓名长度必须为1到10")
    @NotEmpty(message = "{demo.key.null}")
    private String name;
}
