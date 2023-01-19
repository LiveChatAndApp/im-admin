package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomQueryForm {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "聊天室ID")
    private String cid;

    @ApiModelProperty(value = "聊天室名称")
    private String name;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "注册开始时间(yyyy-MM-dd HH:mm:ss)")
    private String createTimeGt;

    @ApiModelProperty(value = "注册结束时间(yyyy-MM-dd HH:mm:ss)")
    private String createTimeLe;
}
