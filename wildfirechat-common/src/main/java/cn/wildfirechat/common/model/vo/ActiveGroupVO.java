package cn.wildfirechat.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Object 活跃群组传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "活跃群组")
public class ActiveGroupVO {

    @ApiModelProperty(value = "排名")
    private Integer range;

    @ApiModelProperty(value = "群組ID")
    private String gid;

    @ApiModelProperty(value = "群組名稱")
    private String name;

    @ApiModelProperty(value = "消息数量")
    private Long messageCount;


}
