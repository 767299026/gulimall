package com.lsl.gulimall.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulimallFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallFileApplication.class, args);
    }

}
