package shop.itbook.itbookbatch.coupon.sceduler;

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
import shop.itbook.itbookbatch.coupon.birthdaycoupon.config.BirthdayCouponProvidingJobConfig;
import shop.itbook.itbookbatch.coupon.birthdaycoupon.exception.BirthdayCouponProvideRunTimeException;
import shop.itbook.itbookbatch.grade.membershippoint.config.PointProvidingJobConfig;
import shop.itbook.itbookbatch.grade.membershippoint.exception.GradePointProvideRunTimeException;

/**
 * @author 송다혜
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class CouponScheduler {
    private final JobLauncher jobLauncher;
    private final BirthdayCouponProvidingJobConfig birthdayCouponProvidingJobConfig;
    @Scheduled(cron = "0 0 * * * *")
    public void doBirthdayCouponProviding() throws Exception {
        Job job = birthdayCouponProvidingJobConfig.birthdayCouponProvidingJob();
        JobParameters jobParameters = new JobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | JobRestartException e) {
            throw new BirthdayCouponProvideRunTimeException();
        }
    }
}
