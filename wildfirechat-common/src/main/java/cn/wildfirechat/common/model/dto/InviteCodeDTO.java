package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Data Transfer Object 邀请码数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeDTO {
    private Long id;// 流水号ID
    private String inviteCode; // 邀请码
    private Integer friendsDefault; // 预设好友模式 1: 所有, 2: 轮询[InviteCodeDefaultFriendTypeEnum]
    private Integer status; // 状态 0: 停用, 1: 使用中 [InviteCodeStatusEnum]
    private String memo; // 后台备注
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
    private Long creator; // 创建者
    private Long updater; // 修改者
}
