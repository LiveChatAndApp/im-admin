package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Query Object 充值订单查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeChannelQuery {

    private Integer paymentMethod;// 充值方式 1:银行卡,2:微信,3:支付宝
    private Integer status;// 状态 1:待审查,2:已完成,3:已拒绝
    private Date createTimeGe;
    private Date createTimeLe;
}
