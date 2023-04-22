package ru.soloviev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@RestController
@EntityScan(basePackages = {"ru.soloviev.Entities"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
