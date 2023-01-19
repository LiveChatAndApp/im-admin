package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object 充值订单数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeOrderDTO {
    private Long rechargeOrderId;// 充值订单ID。
    private String orderCode;// 订单编号
    private String memberName;// 用户账号
    private String nickName;// 昵称
    private Integer method;// 充值方式 1:银行卡,2:微信,3:支付宝
    private BigDecimal amount;// 充值金额
    private String currency;// 币种
    private Long channelId;// 渠道ID
    private String payImage;//付款截图
    private Date createTime;// 提交时间
    private Date completeTime;// 完成时间
    private Integer status;// 状态 1:待审查,2:已完成,3:已拒绝,4:用户取消,5:订单超时

    private String channelName;// 渠道名称
    private String channelInfo;// 渠道资讯
}
