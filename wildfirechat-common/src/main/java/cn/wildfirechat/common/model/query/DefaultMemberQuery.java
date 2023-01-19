package cn.wildfirechat.common.model.query;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * Query Object 预设好友/群查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMemberQuery implements Serializable {
    private Long id;// 流水号ID
    private Integer defaultType;// 预设类型 好友:1, 群:2 [DefaultMemberDefaultTypeEnum]
    private Long memberId; // 预设好友ID
    private Long groupId; // 预设群ID
    private String welcomeText; // 欢迎词
    private Integer type; // 类型 1:所有新注册用户, 2:使用邀请码注册用户 [DefaultMemberTypeEnum]
    private Long inviteCodeId; // 邀请码ID
    private Long creator; // 创建者ID
    private Long updater; // 修改者ID
    private Date createTimeGt; // 创建开始时间
    private Date createTimeLe; // 创建结束时间
    private Date updateTimeGt; // 修改开始时间
    private Date updateTimeLe; // 修改结束时间
}
