package shop.itbook.itbookbatch.grade.membershippoint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointReadListener;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointWriteListener;

/**
 * @author 최겸준
 * @since 1.0
 */
@Configuration
public class CustomPointListener {

    @Bean
    public CustomPointReadListener customCouponItemReadListener() {
        return new CustomPointReadListener();
    }

    @Bean
    public CustomPointWriteListener customCouponItemWriteListener() {
        return new CustomPointWriteListener();
    }
}
