package shop.itbook.itbookbatch.coupon.birthdaycoupon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.itbook.itbookbatch.coupon.birthdaycoupon.listener.CustomCouponReadListener;
import shop.itbook.itbookbatch.coupon.birthdaycoupon.listener.CustomCouponWriteListener;

/**
 * @author 송다혜
 * @since 1.0
 */
@Configuration
public class CustomCouponListener {

    @Bean
    public CustomCouponReadListener customCouponItemReadListener(){
        return new CustomCouponReadListener();
    }

    @Bean
    public CustomCouponWriteListener customCouponItemWriteListener(){
        return new CustomCouponWriteListener();
    }
}
