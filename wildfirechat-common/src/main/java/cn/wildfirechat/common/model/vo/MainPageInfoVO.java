package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * View Object 首页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "首頁資訊")
public class MainPageInfoVO {

    @ApiModelProperty(value = "活跃用户数")
    private Long activeMemberCount;

    @ApiModelProperty(value = "新增用户数")
    private Integer addMemberCount;

    @ApiModelProperty(value = "新建群个数")
    private Integer addGroupCount;

    @ApiModelProperty(value = "发送消息数")
    private Long messageCount;

    @ApiModelProperty(value = "当前系统总用户数")
    private Integer memberGrandTotalCount;

    @ApiModelProperty(value = "当前系统群总数")
    private Integer groupGrandTotalCount;

    @ApiModelProperty(value = "Top10活跃用户数")
    private Collection<ActiveMemberVO> top10ActiveMember;

    @ApiModelProperty(value = "Top10活跃群组")
    private List<ActiveGroupVO> top10ActiveGroup;

}
