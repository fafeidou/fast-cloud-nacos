package fast.cloud.nacos.pojo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Dog implements Animal {
    @Override
    public void use() {
        System.out.println("狗【" + Dog.class.getSimpleName() + " 】是看门用的。");
    }
}
