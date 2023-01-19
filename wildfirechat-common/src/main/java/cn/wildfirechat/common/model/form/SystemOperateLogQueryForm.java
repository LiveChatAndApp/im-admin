package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemOperateLogQueryForm {

    @ApiModelProperty(value = "管理员操作帐号")
    private String userName;// 帐号

    @ApiModelProperty(value = "操作行为")
    private Long authId;// 请求方法

    @ApiModelProperty(value = "IP地址")
    private String creatorIp;// IP地址

    @ApiModelProperty(value = "操作开始时间", example = "2022-10-18 16:00:00")
    private String createTimeGt;

    @ApiModelProperty(value = "操作结束时间", example = "2022-10-18 17:00:00")
    private String createTimeLe;
}
