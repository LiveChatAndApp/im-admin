package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Business Object 提现订单业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderBO {
    private Long id;// ID
    private Integer status;// 状态 1:待审核,2:已完成,3:已拒绝,4:用户取消
    private Long updaterId;// 最后修改者ID
    private Integer updaterRole;// 最后修改者角色 1: 系统管理者, 2: 会员
}
