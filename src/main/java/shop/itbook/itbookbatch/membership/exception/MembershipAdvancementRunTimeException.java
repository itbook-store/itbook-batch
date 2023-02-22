package shop.itbook.itbookbatch.membership.exception;

/**
 * @author 노수연
 * @since 1.0
 */
public class MembershipAdvancementRunTimeException extends RuntimeException {

    public static final String MESSAGE = "회원 등급 승급에 문제가 발생하였습니다.";

    public MembershipAdvancementRunTimeException() {
        super(MESSAGE);
    }
}
