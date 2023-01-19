package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 充值订单持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderPO {
    private Long id;// ID
    private String orderCode;// 订单编号
    private Long userId;// 用户ID
    private Integer method;// 充值方式 1:线下支付,2:微信,3:支付宝
    private BigDecimal amount;// 充值金额
    private String currency;// 币种
    private Long channelId;// 渠道ID
    private String payImage;//付款截图
    private Integer status;// 状态 1:待审查,2:已完成,3:已拒绝,4:用户取消,5:订单超时
    private Date createTime;// 提交时间
    private Date completeTime;// 完成时间
    private Long updaterId;// 最后修改者ID
    private Integer updaterRole;// 最后修改者角色 1: 系统管理者, 2: 会员
    private Date updateTime;// 最后修改时间
}
