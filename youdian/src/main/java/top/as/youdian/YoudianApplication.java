package top.as.youdian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@MapperScan({"top.as.youdian.dao"})
public class YoudianApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoudianApplication.class, args);
    }

}
