package shop.itbook.itbookbatch.membership.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.itbook.itbookbatch.membership.config.MembershipAdvancementJobConfig;
import shop.itbook.itbookbatch.membership.exception.MembershipAdvancementRunTimeException;

/**
 * @author 노수연
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class MembershipScheduler {

    private final JobLauncher jobLauncher;

    private final MembershipAdvancementJobConfig membershipAdvancementJobConfig;

    @Scheduled(cron = "0 0 0 1 * *")
    public void doMembershipAdvancement() throws Exception {
        Job job = membershipAdvancementJobConfig.membershipAdvancementJob();
        JobParameters jobParameters = new JobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
        | JobParametersInvalidException | JobRestartException e) {
            throw new MembershipAdvancementRunTimeException();
        }
    }

}
