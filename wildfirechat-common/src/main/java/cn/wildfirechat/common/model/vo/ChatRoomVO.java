package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object 聊天室数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("聊天室列表")
public class ChatRoomVO {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "聊天室ID")
    private String cid;

    @ApiModelProperty(value = "聊天室名称(聊天室信息)")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图挡路径")
    private String image;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "发言状态 0:停用,1:启用")
    private Integer chatStatus;

    @ApiModelProperty(value = "聊天室详情描述")
    private String desc;

    @ApiModelProperty(value = "附加信息")
    private String extra;

    @ApiModelProperty(value = "在线人数")
    private Integer onlineCount;


}
