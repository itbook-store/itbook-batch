package shop.itbook.itbookbatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Mysql data source 관련 설정 Configuration 입니다.
 *
 * @author 최겸준
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
@Getter
@Setter
public class DataSourceConfig {
    @Value("${shopping-mall-datasource.url}")
    private String shopUrl;
    @Value("${datasource.data-source-class-name}")
    private String dataSourceClassName;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;


    /**
     * shopping mall db 설정을 위한 DataSource 빈 등록 메서드 입니다.
     *
     * @return shopping mall db 관련 DataSource 를 반환합니다.
     */
    @Primary
    @BatchDataSource

    @Bean(name = "shopDataSource")
    public DataSource shopDataSource() {

        return getDataSource(shopUrl);
    }


    private DataSource getDataSource(String url) {

        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.type(HikariDataSource.class);
        builder.url(url);
        builder.username(username);
        builder.password(password);
        builder.driverClassName(dataSourceClassName);
//
//        Properties properties = new Properties();
//        properties.setProperty("url", url);
//        properties.setProperty("user", username);
//        properties.setProperty("password", password);
//
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDataSourceClassName(dataSourceClassName);
//        hikariConfig.setMaximumPoolSize(2);
//        hikariConfig.setDataSourceProperties(properties);
//        hikariConfig.setConnectionTestQuery("select 1");

//        return new HikariDataxSource(hikariConfig);
        return builder.build();
    }


    /**
     * shopDataSource 의 DataSourceTransactionManager 를 빈 등록하는 메서드 입니다.
     *
     * @return ShopDataSource 로 지정된 DataSourceTransactionManager 를 반환합니다.
     */
    @Bean
    public PlatformTransactionManager shopTransactionManager() {
        return new DataSourceTransactionManager(shopDataSource());
    }

    /**
     * 쇼핑몰 transaction 을 chaining 하여
     * PlatformTransactionManager 를 반환하는 빈 등록 메서드입니다.
     *
     * @param shopTransactionManager   쇼핑몰 db 관련 transactionManager 입니다.
    * @return 쇼핑몰 batch transaction 을 chaining 한 PlatformTransactionManager 를 반환합니다.
     */
    @Primary
    @Bean
    public PlatformTransactionManager customTransactionManager(
            @Qualifier("shopTransactionManager") PlatformTransactionManager
                    shopTransactionManager) {

        return new ChainedTransactionManager(
                shopTransactionManager);
    }

}
