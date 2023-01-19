package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object 提现订单数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderDTO {
    private Long withdrawOrderId;// 提现订单流水ID
    private String orderCode;// 订单编号
    private Long userId;// 用户ID
    private String nickName;// 用户昵称
    private String memberName;// 用户账号
    private String currency;// 交易货币
    private Integer channel;// 提现渠道 1:银行卡,2:游戏平台
    private String accountInfo;// 帐号资讯
    private BigDecimal amount;// 交易金额
    private Integer status;// 状态 1:待审核,2:已完成,3:已拒绝
    private Date createTime;// 交易时间
    private Date completeTime;// 完成时间
}
