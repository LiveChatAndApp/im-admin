package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Form 聊天編輯表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUpdateForm {

    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "聊天室ID", required = true)
    private String cid;

    @NotNull
    @ApiModelProperty(value = "聊天室排序", required = true)
    private Integer sort;

}
