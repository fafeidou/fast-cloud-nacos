package fast.cloud.nacos.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BizPerson implements Person {


    private Animal animal;

    @Override
    public void service() {
        animal.use();
    }

    @Override
    @Autowired
    @Qualifier("cat")
    public void setAnimal(Animal animal) {
        System.out.println("延迟依赖注入");
        this.animal = animal;
    }
}
