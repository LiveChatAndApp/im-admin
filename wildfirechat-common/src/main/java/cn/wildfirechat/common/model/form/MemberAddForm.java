package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddForm {
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "头像")
    private MultipartFile avatarFile;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别 1: 保密, 2: 男, 3: 女")
    @Min(value = 1, message = "Parameter [gender] cannot be less than 1")
    @Max(value = 3, message = "Parameter [gender] cannot be greater than 3")
    private Integer gender;

    @ApiModelProperty(value = "备注")
    private String memo;
}
