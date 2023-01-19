package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 敏感词业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordAddBO {

    private String content;//敏感词内容
}
