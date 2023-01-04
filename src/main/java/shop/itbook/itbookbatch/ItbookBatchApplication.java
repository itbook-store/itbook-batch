package shop.itbook.itbookbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ItbookBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItbookBatchApplication.class, args);
    }
}
