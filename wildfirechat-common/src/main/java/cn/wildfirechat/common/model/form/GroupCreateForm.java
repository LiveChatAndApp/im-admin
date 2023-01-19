package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Form 群新增表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateForm {
    @ApiModelProperty(value = "群组名称", required = true)
    private String groupName;

    @ApiModelProperty(value = "头像")
    private MultipartFile avatarFile;

    @ApiModelProperty(value = "群主用户帐号", required = true)
    private String memberName;

    @ApiModelProperty(value = "群组类型 1:一般,2:广播", required = true)
    private Integer groupType;
}
