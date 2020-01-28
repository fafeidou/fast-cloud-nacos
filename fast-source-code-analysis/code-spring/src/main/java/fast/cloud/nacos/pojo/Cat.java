package fast.cloud.nacos.pojo;

import org.springframework.stereotype.Component;

@Component
public class Cat implements Animal {
    @Override
    public void use() {
        System.out.println("猫【" + Dog.class.getSimpleName() + " 】是用来抓老鼠的。");
    }
}
