package shop.itbook.itbookbatch.grade.membershippoint.config;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointReadListener;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointWriteListener;

/**
 * @author 최겸준
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PointProvidingJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final PointHistorySavingStepConfig pointHistorySavingStepConfig;
    private final GradePointHistorySavingStepConfig gradePointHistorySavingStepConfig;

    @Bean
    public Job gradePointProvidingJob() throws Exception {

        return jobBuilderFactory.get("등급별 등급 포인트 지급(적립)_" + LocalDateTime.now())
            .start(pointHistorySavingStepConfig.pointHistorySavingStep())
            .on("FAILED").fail()
            .from(pointHistorySavingStepConfig.pointHistorySavingStep())
            .on("*").end()
            .next(gradePointHistorySavingStepConfig.gradePointHistorySavingStep())
            .end()
            .build();
    }
}
