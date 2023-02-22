package shop.itbook.itbookbatch.membership.config;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 노수연
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class MembershipAdvancementJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final MembershipHistorySavingStepConfig membershipHistorySavingStepConfig;

    @Bean
    public Job membershipAdvancementJob() throws Exception {

        return jobBuilderFactory.get("주문금액별 membership_history 등록 step_" + LocalDateTime.now())
            .start(membershipHistorySavingStepConfig.membershipHistorySavingStep())
            .on("FAILED").fail()
            .from(membershipHistorySavingStepConfig.membershipHistorySavingStep())
            .on("*").end()
            .end()
            .build();
    }
}
