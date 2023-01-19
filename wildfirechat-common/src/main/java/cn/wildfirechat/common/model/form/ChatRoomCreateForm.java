package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Form 聊天新增表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateForm {

    @NotNull
    @ApiModelProperty(value = "聊天室名称", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "头像")
    private MultipartFile imageFile;

}
