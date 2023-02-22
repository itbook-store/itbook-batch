package shop.itbook.itbookbatch.membership.config;

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
import shop.itbook.itbookbatch.membership.dto.MembershipHistorySavePrepareDto;

/**
 * @author 노수연
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class MembershipHistorySavingStepConfig {

    private final CustomMembershipListener customMembershipListener;

    private final DataSource shopDataSource;

    private final StepBuilderFactory stepBuilderFactory;

    private static final Integer CHUNK_SIZE = 1000;

    @Bean
    @JobScope
    public Step membershipHistorySavingStep() throws Exception {

        return stepBuilderFactory.get("주문금액별 membership_history 등록 step_" + LocalDateTime.now())
            .allowStartIfComplete(true)
            .<MembershipHistorySavePrepareDto, MembershipHistorySavePrepareDto>chunk(CHUNK_SIZE)
            .reader(getMembershipHistorySaveDtoListReader())
            .writer(saveMembershipHistoryWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(2)
            .listener(customMembershipListener.customMembershipReadListener())
            .listener(customMembershipListener.customMembershipWriterListener())
            .build();

    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<MembershipHistorySavePrepareDto> getMembershipHistorySaveDtoListReader() throws Exception {

        return new JdbcPagingItemReaderBuilder<MembershipHistorySavePrepareDto>()
            .pageSize(CHUNK_SIZE)
            .dataSource(shopDataSource)
            .rowMapper(new BeanPropertyRowMapper<>(MembershipHistorySavePrepareDto.class))
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

        queryProviderFactoryBean.setSelectClause("SELECT m.member_no as memberNo, ifnull((select membership_no from membership where membership_standard_amount < sum(op.amount) order by membership_standard_amount desc limit 1), 2029) as membershipNo, ifnull(sum(op.amount), 0) as monthlyUsageAmount ");
        queryProviderFactoryBean.setFromClause("FROM member as m left outer join order_paper_member as opm on m.member_no = opm.member_no left outer join order_paper as op on opm.order_no = op.order_no ");
        queryProviderFactoryBean.setWhereClause("where op.order_created_at is null or (op.order_created_at > LAST_DAY(NOW() - interval 2 month) + interval 1 Day)");
        queryProviderFactoryBean.setGroupClause("group by m.member_no ");

        queryProviderFactoryBean
            .setSortKeys(sorts);

        return queryProviderFactoryBean.getObject();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MembershipHistorySavePrepareDto> saveMembershipHistoryWriter() {

        StringBuilder sql = new StringBuilder();
        sql.append(String.format("insert into membership_history values (null, :memberNo, :membershipNo, :monthlyUsageAmount, now())"));

        return new JdbcBatchItemWriterBuilder<MembershipHistorySavePrepareDto>()
            .dataSource(shopDataSource)
            .sql(sql.toString())
            .beanMapped()
            .build();

    }
}
