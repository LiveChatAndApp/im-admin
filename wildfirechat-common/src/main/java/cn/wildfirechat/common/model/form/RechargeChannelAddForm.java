package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechargeChannelAddForm {

    @NotNull
    @ApiModelProperty(value = "充值渠道名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "收款方式 1:线下支付,2:微信,3:支付宝")
    private Integer paymentMethod;

    @NotNull
    @ApiModelProperty(value = "状态 0:停用,1:启用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "QRCode上传图")
    private MultipartFile qrCodeImageFile;

}
