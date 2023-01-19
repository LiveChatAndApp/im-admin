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
public class SensitiveWordQueryForm {

    @ApiModelProperty(value = "敏感词内容")
    private String content;
}
