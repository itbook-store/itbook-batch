package shop.itbook.itbookbatch.grade.membershippoint.config;

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
import shop.itbook.itbookbatch.grade.membershippoint.dto.GradePointHistorySavePrepareDto;
import shop.itbook.itbookbatch.grade.membershippoint.gradepointenum.GradePointContentEnum;

/**
 * @author 최겸준
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class GradePointHistorySavingStepConfig {

    private final CustomPointListener customPointListener;
    private final DataSource shopDataSource;

    private final StepBuilderFactory stepBuilderFactory;

    private static final Integer CHUNK_SIZE = 1000;

    @Bean
    @JobScope
    public Step gradePointHistorySavingStep() throws Exception {

        return stepBuilderFactory.get("회원등급별 grade_point_history 등록 step_" + LocalDateTime.now())
            .allowStartIfComplete(true)
            .<GradePointHistorySavePrepareDto, GradePointHistorySavePrepareDto>chunk(CHUNK_SIZE)
            .reader(getGradePointHistorySaveDtoListReader())
            .writer(saveGradePointHistoryWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(2)
            .listener(customPointListener.customCouponItemReadListener())
            .listener(customPointListener.customCouponItemWriteListener())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<GradePointHistorySavePrepareDto> getGradePointHistorySaveDtoListReader() throws Exception {

        return new JdbcPagingItemReaderBuilder<GradePointHistorySavePrepareDto>()
            .pageSize(CHUNK_SIZE)
            .dataSource(shopDataSource)
            .rowMapper(new BeanPropertyRowMapper<>(GradePointHistorySavePrepareDto.class))
            .queryProvider(customQueryProvider())
            .name("jdbcPagingItemReader")
            .build();
    }

    public PagingQueryProvider customQueryProvider() throws Exception {

        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean =
            new SqlPagingQueryProviderFactoryBean();

        Map<String, Order> sorts = new HashMap<>();
        sorts.put("pointHistoryNo", Order.ASCENDING);

        queryProviderFactoryBean.setDataSource(shopDataSource);
        StringBuilder fromSql = new StringBuilder();

        queryProviderFactoryBean.setSelectClause("select max(ph.point_history_no) as pointHistoryNo, m.membership_no as membershipNo ");
        fromSql.append("from point_history ph ")
            .append("inner join member m ")
            .append("on m.member_no = ph.member_no ");
        queryProviderFactoryBean.setFromClause(fromSql.toString());
        queryProviderFactoryBean.setWhereClause(String.format("where point_increase_decrease_content_no = %d ", GradePointContentEnum.MEMBERSHIP.getMembershipNo()));
        queryProviderFactoryBean.setGroupClause("group by ph.member_no");

        queryProviderFactoryBean
            .setSortKeys(sorts);

        return queryProviderFactoryBean.getObject();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<GradePointHistorySavePrepareDto> saveGradePointHistoryWriter() {

        return new JdbcBatchItemWriterBuilder<GradePointHistorySavePrepareDto>()
            .dataSource(shopDataSource)
            .sql("insert into grade_increase_point_history values (:pointHistoryNo, :membershipNo)")
            .beanMapped()
            .build();
    }
}
