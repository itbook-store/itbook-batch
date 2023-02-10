package shop.itbook.itbookbatch.coupon.birthdaycoupon.exception;

/**
 * @author 송다혜
 * @since 1.0
 */
public class BirthdayCouponProvideRunTimeException extends RuntimeException {

    public static final String MESSAGE = "생일 쿠폰 제공중에 문제가 발생했습니다.";

    public BirthdayCouponProvideRunTimeException() {
        super(MESSAGE);
    }
}
