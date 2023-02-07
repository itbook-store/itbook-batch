package shop.itbook.itbookbatch.grade.membershippoint.gradepointenum;

import lombok.Getter;

/**
 * @author 최겸준
 * @since 1.0
 */
@Getter
public enum GradePointContentEnum {
    MEMBERSHIP(4);

    private Integer membershipNo;

    GradePointContentEnum(Integer membershipNo) {
        this.membershipNo = membershipNo;
    }
}
