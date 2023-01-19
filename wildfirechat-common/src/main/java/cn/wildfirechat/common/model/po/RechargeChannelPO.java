package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Persistant Object 充值渠道持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeChannelPO {
    private Long id;// ID
    private String name;// 充值渠道名称
    private Integer paymentMethod;// 收款方式 1:线下支付,2:微信,3:支付宝
    private String info;// 收款方式资讯
    private String qrCodeImage;// QRCode路径
    private Integer status;// 状态 状态 0:停用,1:启用
    private String memo;// 备注
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
}
