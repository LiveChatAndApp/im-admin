package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object 会员馀额日志数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalanceLogDTO {
    private Long memberBalanceId;// 资金明细流水ID
    private Long userId;// 用户ID
    private String uid;// 用户UID
    private String nickName;// 用户昵称
    private String memberName;// 帳號
    private String currency;// 交易货币
    private BigDecimal amount;// 交易金额
    private BigDecimal beforeBalance;// 交易前馀额
    private BigDecimal afterBalance;// 交易后馀额
    private BigDecimal beforeFreeze;// 交易前冻结金额
    private BigDecimal afterFreeze;// 交易后冻结金额
    private Date createTime;// 交易时间
    private Integer type;// 类型 1:手动充值,2:手动提取
    private String memo;// 备注
}
