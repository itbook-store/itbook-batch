package shop.itbook.itbookbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * batch 관련 설정.
 *
 * @author 최겸준
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRegistry jobRegistry;

    /**
     * JobRegistry 를 자동으로 저장시키기 위한 bean 설정 메서드.
     *
     * @return the bean post processor
     */
    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor =
                new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
