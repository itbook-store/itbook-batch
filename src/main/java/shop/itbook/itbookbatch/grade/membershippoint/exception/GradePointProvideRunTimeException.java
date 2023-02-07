package shop.itbook.itbookbatch.grade.membershippoint.exception;

/**
 * @author 최겸준
 * @since 1.0
 */
public class GradePointProvideRunTimeException extends RuntimeException {

    public static final String MESSAGE = "회원 등급별 포인트 제공중에 문제가 발생했습니다.";

    public GradePointProvideRunTimeException() {
        super(MESSAGE);
    }
}
