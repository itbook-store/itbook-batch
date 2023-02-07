package shop.itbook.itbookbatch.grade.membershippoint.config;

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
import shop.itbook.itbookbatch.grade.membershippoint.gradepointenum.GradePointContentEnum;
import shop.itbook.itbookbatch.grade.membershippoint.dto.PointHistorySavePrepareDto;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointReadListener;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointWriteListener;

/**
 * @author 최겸준
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PointHistorySavingStepConfig {

    private final CustomPointListener customPointListener;

    private final DataSource shopDataSource;

    private final StepBuilderFactory stepBuilderFactory;

    private static final Integer CHUNK_SIZE = 1000;

    private static final Integer POINT_INCREASE = 0;


    @Bean
    @JobScope
    public Step pointHistorySavingStep() throws Exception {

        return stepBuilderFactory.get("회원등급별 point_history 등록 step")
            .allowStartIfComplete(true)
            .<PointHistorySavePrepareDto, PointHistorySavePrepareDto>chunk(CHUNK_SIZE)
            .reader(getPointHistorySaveDtoListReader())
            .writer(savePointHistoryWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(2)
            .listener(customPointListener.customCouponItemReadListener())
            .listener(customPointListener.customCouponItemWriteListener())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<PointHistorySavePrepareDto> getPointHistorySaveDtoListReader() throws Exception {

        return new JdbcPagingItemReaderBuilder<PointHistorySavePrepareDto>()
            .pageSize(CHUNK_SIZE)
            .dataSource(shopDataSource)
            .rowMapper(new BeanPropertyRowMapper<>(PointHistorySavePrepareDto.class))
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

        queryProviderFactoryBean.setSelectClause("SELECT  m.member_no as memberNo, ms.membership_point as membershipPoint ");
        queryProviderFactoryBean.setFromClause("FROM membership ms inner join member m on ms.membership_no=m.membership_no");

        queryProviderFactoryBean
            .setSortKeys(sorts);

        return queryProviderFactoryBean.getObject();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<PointHistorySavePrepareDto> savePointHistoryWriter() {

        StringBuilder sql = new StringBuilder();
        sql.append(String.format("insert into point_history values (null, :memberNo, %d, :membershipPoint, ", GradePointContentEnum.MEMBERSHIP.getMembershipNo()))
            .append(String.format("ifnull((select p.remained_point + :membershipPoint from point_history p where p.member_no = :memberNo order by p.point_history_no desc limit 1), :membershipPoint), now(), %d)", POINT_INCREASE));

        return new JdbcBatchItemWriterBuilder<PointHistorySavePrepareDto>()
            .dataSource(shopDataSource)
            .sql(sql.toString())
            .beanMapped()
            .build();
    }


}
