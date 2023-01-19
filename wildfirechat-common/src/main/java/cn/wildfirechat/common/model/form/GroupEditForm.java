package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Form 群编辑表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupEditForm {
    @ApiModelProperty(value = "群組ID")
    private String groupId;

    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "头像")
    private MultipartFile avatarFile;

    @ApiModelProperty(value = "群主用户帐号")
    private String memberName;
}
