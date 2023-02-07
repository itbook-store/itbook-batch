package shop.itbook.itbookbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
@EnableBatchProcessing
public class ItbookBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItbookBatchApplication.class, args);
    }
}
