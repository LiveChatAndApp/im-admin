package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 敏感词搜寻表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordHitQueryForm {

    @ApiModelProperty(value = "发送者帐号")
    private String senderAccount;
}
