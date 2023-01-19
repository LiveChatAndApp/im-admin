package cn.wildfirechat.common.model.vo;

import cn.wildfirechat.common.model.dto.RechargeChannelInfoDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * View Object 提现订单显示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("充值订单")
public class RechargeOrderVO {
    private Long rechargeOrderId;// 充值订单ID。
    private String orderCode;// 订单编号
    private String memberName;// 用户账号
    private String nickName;// 昵称
    private Integer method;// 充值方式 1:银行卡,2:微信,3:支付宝
    private String amount;// 充值金额
    private String currency;// 币种
    private Long channelId;// 渠道ID
    private String payImage;// 付款截图
    private String createTime;// 提交时间
    private String completeTime;// 完成时间
    private Integer status;// 状态 1:待审查,2:已完成,3:已拒绝,4:用户取消,5:订单超时

    private String channelName;// 充值渠道名称
    private RechargeChannelInfoDTO channelInfo;// 渠道名称

}
