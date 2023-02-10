package shop.itbook.itbookbatch.coupon.birthdaycoupon.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

/**
 * @author 송다혜
 * @since 1.0
 */
@Slf4j
public class CustomCouponReadListener implements ItemReadListener {

    @Override
    public void beforeRead() {
        log.debug("coupon item read start!");
    }

    @Override
    public void afterRead(Object o) {
        log.debug("coupon item read end!");
    }

    @Override
    public void onReadError(Exception e) {
        log.error("coupon item read error! error : " + e.getMessage());
    }
}
