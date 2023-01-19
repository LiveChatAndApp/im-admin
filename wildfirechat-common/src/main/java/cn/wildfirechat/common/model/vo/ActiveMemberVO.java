package cn.wildfirechat.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Object 活跃用户传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "活跃用户")
public class ActiveMemberVO {

    @ApiModelProperty(value = "排名")
    private Integer range;

    @ApiModelProperty(value = "用户账号")
    private String memberName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "消息数量")
    private Long messageCount;


}
