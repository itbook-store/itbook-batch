package shop.itbook.itbookbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItbookBatchApplication {

    public static void main(String[] args) {
        System.out.println("Jenkins 배포");
        System.out.println("Jenkins 배포2");
        System.out.println("제발");
        SpringApplication.run(ItbookBatchApplication.class, args);
    }

}
