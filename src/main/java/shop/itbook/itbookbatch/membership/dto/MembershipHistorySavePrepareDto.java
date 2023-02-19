package shop.itbook.itbookbatch.membership.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 노수연
 * @since 1.0
 */
@Getter
@Setter
public class MembershipHistorySavePrepareDto {
    private Long memberNo;
    private Integer membershipNo;
    private Long monthlyUsageAmount;
}
