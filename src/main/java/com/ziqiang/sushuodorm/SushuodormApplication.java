package com.ziqiang.sushuodorm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ziqiang.sushuodorm"})
@MapperScan("com.ziqiang.sushuodorm.mapper")
public class SushuodormApplication {

    public static void main(String[] args) {
        SpringApplication.run(SushuodormApplication.class, args);
    }

}
