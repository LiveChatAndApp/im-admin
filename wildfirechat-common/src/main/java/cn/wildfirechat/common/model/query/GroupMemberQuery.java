package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * Query Object 群成员查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberQuery implements Serializable {

    private Long id;// ID

    private Long memberId;// 会员ID

    private Long groupId; // 群ID

    private Integer memberType; // 成员类型 1: 成员, 2: 管理员, 3: 群主 [GroupMemberTypeEnum]

    private Integer verify; // 验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除 [RelateVerifyEnum]

    private String verifyText; // 验证消息

    private Date createTimeGt;// 创建时间

    private Date createTimeLe;// 创建时间

    private Date updateTimeGt;// 最后修改时间

    private Date updateTimeLe;// 最后修改时间

    private String uid; // 用戶ID

    private String member; // 用戶帐号/昵称
}
