package shop.itbook.itbookbatch.grade.scheduler;

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
import shop.itbook.itbookbatch.grade.membershippoint.config.PointProvidingJobConfig;
import shop.itbook.itbookbatch.grade.membershippoint.exception.GradePointProvideRunTimeException;

/**
 * 회원 등급 갱신 및 포인트 지급 job scheduler.
 *
 * @author 최겸준
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class GradeScheduler {
    private final JobLauncher jobLauncher;
    private final PointProvidingJobConfig pointProvidingJobConfig;

    @Scheduled(cron = "0 0 0 * * *")
    public void doGradeRenewalAndGradePointProviding() throws Exception {
        Job job = pointProvidingJobConfig.gradePointProvidingJob();
        JobParameters jobParameters = new JobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | JobRestartException e) {
            throw new GradePointProvideRunTimeException();
        }
    }
}
