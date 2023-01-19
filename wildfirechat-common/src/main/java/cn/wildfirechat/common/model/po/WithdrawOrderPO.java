package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 提现订单持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderPO {
    private Long id;// ID
    private String orderCode;// 订单编号
    private Long userId;// 用户ID
    private String currency;// 币种
    private Integer channel;// 提现渠道 1:银行卡,2:游戏平台
    private String accountInfo;// 账号资讯
    private BigDecimal amount;// 提现金额
    private Integer status;// 状态 1:待审核,2:已完成,3:已拒绝,4:用户取消
    private Date createTime;// 创建时间
    private Date completeTime;// 完成时间
    private Long updaterId;// 最后修改者ID
    private Integer updaterRole;// 最后修改者角色 1: 系统管理者, 2: 会员
    private Date updateTime;// 最后修改时间
}
