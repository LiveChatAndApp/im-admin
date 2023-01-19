package cn.wildfirechat.common.model.po;

import cn.wildfirechat.common.model.vo.ActiveMemberVO;
import cn.wildfirechat.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Persistant Object 首页资讯
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainPageInfoPO {

    private Long id;// ID

    private Long activeMemberCount;// 活跃用户数

    private Integer addMemberCount;// 新增用户数

    private Integer addGroupCount;// 新建群个数

    private Long messageCount;// 发送消息数

    private String top10ActiveMember;// Top10活跃用户数

    private String top10ActiveGroup;// Top10活跃群组

    private Integer memberGrandTotalCount;//当前系统总用户数

    private Integer groupGrandTotalCount;//当前系统群总数

    private Date createTime;// 创建时间

}
