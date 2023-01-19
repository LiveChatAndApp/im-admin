package cn.wildfirechat.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object 会员详情传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "会员详细资料")
public class MemberDetailVO {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("UID")
    private String uid;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("账号")
    private String memberName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("个性签名")
    private String signature;

    @ApiModelProperty("备注")
    private String memo;
}
