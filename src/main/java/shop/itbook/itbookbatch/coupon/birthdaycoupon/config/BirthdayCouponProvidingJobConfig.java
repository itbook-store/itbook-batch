package shop.itbook.itbookbatch.coupon.birthdaycoupon.config;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 송다혜
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class BirthdayCouponProvidingJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final BirthdayCouponIssueSavingStepConfig birthdayCouponIssueSavingStepConfig;

    @Bean
    public Job birthdayCouponProvidingJob() throws Exception{
        return jobBuilderFactory.get("생일 쿠폰 지급_"+ LocalDateTime.now())
            .start(birthdayCouponIssueSavingStepConfig.birthdayCouponIssueSavingStep())
            .on("FAILED").fail()
            .from(birthdayCouponIssueSavingStepConfig.birthdayCouponIssueSavingStep())
            .on("*").end()
            .end()
            .build();

    }
}
