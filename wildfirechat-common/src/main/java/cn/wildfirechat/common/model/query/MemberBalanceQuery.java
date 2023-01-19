package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query Object 系统管理者查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalanceQuery {
    private Long id;// 流水号
    private Long userId;// 用户ID
    private String currency;// 币种
}
