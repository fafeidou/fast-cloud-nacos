package fast.cloud.nacos.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("fast.cloud.nacos.mybatis.mapper")
public class FastCommonMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonMybatisApplication.class, args);
    }

}
