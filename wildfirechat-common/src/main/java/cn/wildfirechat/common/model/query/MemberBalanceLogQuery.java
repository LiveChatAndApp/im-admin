package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Query Object 系统管理者查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBalanceLogQuery {
    private Long id;// 流水号
    private Long userId;// 用户ID
    private String memberName;// 用户帐号
    private String currency;// 币种
    private Integer type;// 类型 1:手动充值,2:手动提取
    private Date createTimeGt;// 交易时间起
    private Date createTimeLe;// 交易时间迄
}
