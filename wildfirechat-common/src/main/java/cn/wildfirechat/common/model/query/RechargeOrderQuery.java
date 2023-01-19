package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Query Object 充值订单查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderQuery {
    private Long id;// ID
    private String orderCode;// 订单编号
    private Long userId;// 用户ID
    private Integer method;// 充值方式 1:线下支付,2:微信,3:支付宝
    private String currency;// 币种
    private Integer status;// 状态 1:待审查,2:已完成,3:已拒绝,4:用户取消,5:订单超时

    private String memberName;// 用户账号
    private Date createTimeGt;// 提交时间起
    private Date createTimeLe;// 提交时间迄
    private Date completeTimeGt;// 完成时间起
    private Date completeTimeLe;// 完成时间迄
}
