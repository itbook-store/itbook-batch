package shop.itbook.itbookbatch.coupon.birthdaycoupon.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import shop.itbook.itbookbatch.coupon.birthdaycoupon.dto.CouponIssueSavePrepareDto;
import shop.itbook.itbookbatch.grade.membershippoint.config.CustomPointListener;
import shop.itbook.itbookbatch.grade.membershippoint.dto.PointHistorySavePrepareDto;
import shop.itbook.itbookbatch.grade.membershippoint.gradepointenum.GradePointContentEnum;

/**
 * @author 송다혜
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class BirthdayCouponIssueSavingStepConfig {
    private final CustomCouponListener customCouponListener;
    private final DataSource shopDataSource;

    private final StepBuilderFactory stepBuilderFactory;

    private static final Integer CHUNK_SIZE = 1000;

    @Bean
    @JobScope
    public Step birthdayCouponIssueSavingStep() throws Exception {

        return stepBuilderFactory.get("생일 쿠폰 coupon_issue 등록 step_" + LocalDateTime.now())
            .allowStartIfComplete(true)
            .<CouponIssueSavePrepareDto, CouponIssueSavePrepareDto>chunk(CHUNK_SIZE)
            .reader(getBirthdayCouponSaveDtoListReader())
            .writer(saveBirthdayCouponIssueWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(2)
            .build();


    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<CouponIssueSavePrepareDto> getBirthdayCouponSaveDtoListReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<CouponIssueSavePrepareDto>()
            .pageSize(CHUNK_SIZE)
            .dataSource(shopDataSource)
            .rowMapper(new BeanPropertyRowMapper<>(CouponIssueSavePrepareDto.class))
            .queryProvider(customQueryProvider())
            .name("jdbcPagingItemReader")
            .build();
    }
    public PagingQueryProvider customQueryProvider() throws Exception {

        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean =
            new SqlPagingQueryProviderFactoryBean();

        Map<String, Order> sorts = new HashMap<>();
        sorts.put("memberNo", Order.ASCENDING);

        queryProviderFactoryBean.setDataSource(shopDataSource);

        queryProviderFactoryBean.setSelectClause(String.format("select c.coupon_no as couponNo, " +
            "c.coupon_expired_at as couponExpiredAt, c.usage_period as usagePeriod, " +
            "m.member_no as memberNo, u.usage_status_no as usageStatusNo"));
        queryProviderFactoryBean.setFromClause(String.format("from coupon c " +
            "inner join coupon_type as ct on c.coupon_type_no = ct.coupon_type_no " +
            "left outer join `member` as m on month(birth) = month(now()) and day(birth) = day(now()) " +
            "left outer join usage_status as u on usage_status_name like '사용가능' " +
            "where ct.coupon_type_name = '생일쿠폰'" +
            "and c.coupon_created_at < now() " +
            "and c.coupon_expired_at > now()"));

        queryProviderFactoryBean
            .setSortKeys(sorts);

        return queryProviderFactoryBean.getObject();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CouponIssueSavePrepareDto> saveBirthdayCouponIssueWriter() {

        StringBuilder sql = new StringBuilder();
        sql.append(String.format("insert into coupon_issue values (null, :memberNo, :couponNo, :usageStatusNo ,now(), null, :couponExpiredAt)"));

        return new JdbcBatchItemWriterBuilder<CouponIssueSavePrepareDto>()
            .dataSource(shopDataSource)
            .sql(sql.toString())
            .beanMapped()
            .build();
    }
}
