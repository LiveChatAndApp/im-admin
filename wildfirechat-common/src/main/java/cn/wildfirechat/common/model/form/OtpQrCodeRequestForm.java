package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpQrCodeRequestForm {

    @NotNull(message = "{adminUserId}不能为空")
    @ApiModelProperty(value="后台用户ID" )
    private Long adminUserId;

    @ApiModelProperty(value="totpCode" )
    private String totpCode;
}
