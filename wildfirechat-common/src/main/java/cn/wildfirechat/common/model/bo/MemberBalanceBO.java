package cn.wildfirechat.common.model.bo;

import cn.wildfirechat.common.model.enums.MemberBalanceLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Business Object 会员馀额日志业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalanceBO {
    private Long userId;// 用户ID
    private Integer type;// 类型 1:手动充值,2:手动提取
    private BigDecimal amount;// 金额
    private BigDecimal freeze;// 冻结金额

    private Long updaterId;// 最后修改者ID

    public void verify() {
        Assert.notNull(userId, "userId is null");

        MemberBalanceLogTypeEnum logTypeEnum = MemberBalanceLogTypeEnum.parse(type);
        Assert.notNull(logTypeEnum, "userId not approved");

        BigDecimal _amount = amount != null ? amount : BigDecimal.ZERO;
        BigDecimal _freeze = freeze != null ? freeze : BigDecimal.ZERO;

        Assert.isTrue(_amount.compareTo(BigDecimal.ZERO) > 0 || _freeze.compareTo(BigDecimal.ZERO) > 0
                , "amount or freeze must be greater than zero");
    }
}
