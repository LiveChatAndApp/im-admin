package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Form 敏感词添加
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordAddForm {

    @ApiModelProperty(value = "敏感词内容")
    private String content;
}
