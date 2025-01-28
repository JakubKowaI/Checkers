// src/main/java/lib/test/Application.java
package lib.test.DB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "lib.test.DB")
@EnableJpaRepositories(basePackages = "lib.test.DB")
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MyService myService = context.getBean(MyService.class);
        myService.createTable();
        myService.insertUser(1, "John Doe");
        System.out.println("User name: " + myService.getUserName(1));
    }
}