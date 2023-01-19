package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Form 预设好友/群搜寻表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMemberQueryPageForm {

    @ApiModelProperty(value = "类型 1:所有新注册用户, 2:使用邀请码注册用户")
    private Integer type;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
}
