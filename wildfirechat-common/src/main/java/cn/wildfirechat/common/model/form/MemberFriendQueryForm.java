package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFriendQueryForm {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "帐号")
    private String memberName;

    @ApiModelProperty("昵称")
    private String nickName;
}
