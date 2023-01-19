package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 会员馀额日志持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalanceLogPO {
    public static final String MANUAL_RECHARGE = "手动充值: {amount}";
    public static final String MANUAL_WITHDRAW = "手动提取: {amount}";
    public static final String MANUAL_RECHARGE_FREEZE = "手动充值: {amount}, 冻结增加: {freeze}";

    public static final String USER_WITHDRAW_APPLY_FREEZE = "用户申请提币: {amount}, 冻结增加: {freeze}";

    public static final String MANUAL_WITHDRAW_FREEZE = "手动提取: {amount}, 冻结减少: {freeze}";
    public static final String USER_WITHDRAW_FREEZE = "用户提取: {amount}, 冻结减少: {freeze}";

    public static final String USER_WITHDRAW_REJECT_FREEZE = "提取拒绝返款: {amount}, 冻结减少: {freeze}";

    public static final String USER_RECHARGE = "用户充值: {amount}";

    private Long id;// ID
    private Long userId;// 用户ID
    private String currency;// 币种
    private Integer type;// 类型 1:手动充值,2:手动提取
    private BigDecimal amount;// 交易金额
    private BigDecimal beforeBalance;// 交易前馀额
    private BigDecimal afterBalance;// 交易后馀额
    private BigDecimal beforeFreeze;// 交易前冻结金额
    private BigDecimal afterFreeze;// 交易后冻结金额
    private String memo;// 备注
    private Date createTime;// 创建时间
}
