package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 会员馀额持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalancePO {
    private Long id;
    private Long userId;
    private String currency;
    private BigDecimal balance;
    private BigDecimal freeze;
    private Date createTime;
    private Date updateTime;
}
