package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 充值订单业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderBO {
    private Long id;// ID
    private Integer status;// 状态 1:待审核,2:已完成,3:已拒绝,4:用户取消,5:订单超时
    private Long updaterId;// 最后修改者ID
    private Integer updaterRole;// 最后修改者角色 1: 系统管理者, 2: 会员
}
