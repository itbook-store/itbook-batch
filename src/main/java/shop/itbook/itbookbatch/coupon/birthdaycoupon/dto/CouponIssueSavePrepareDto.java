package shop.itbook.itbookbatch.coupon.birthdaycoupon.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 송다혜
 * @since 1.0
 */
@Getter
@Setter
public class CouponIssueSavePrepareDto {
    private Long couponNo;
    private Long memberNo;
    private Integer usageStatusNo;
    private LocalDateTime couponExpiredAt;
    private Integer usagePeriod;

}
