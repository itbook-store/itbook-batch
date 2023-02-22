package shop.itbook.itbookbatch.membership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.itbook.itbookbatch.grade.membershippoint.listener.CustomPointWriteListener;
import shop.itbook.itbookbatch.membership.listener.CustomMembershipReadListener;
import shop.itbook.itbookbatch.membership.listener.CustomMembershipWriterListener;

/**
 * @author 노수연
 * @since 1.0
 */
@Configuration
public class CustomMembershipListener {

    @Bean
    public CustomMembershipReadListener customMembershipReadListener() {
        return new CustomMembershipReadListener();
    }

    @Bean
    public CustomMembershipWriterListener customMembershipWriterListener() {
        return new CustomMembershipWriterListener();
    }
}
