package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 朋友查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendQuery implements Serializable {

    private Long id;// 朋友流水号

    private Long memberSourceId;// 邀请者ID

    private Long memberTargetId; // 接受者ID

    private Integer verify; // 验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除 [RelateVerifyEnum]

    private String verifyText; // 验证消息

    private String requestReceiver; // 好友邀请发送者ID

    private Date createTimeGt;// 创建时间

    private Date createTimeLe;// 创建时间

    private Date updateTimeGt;// 最后修改时间

    private Date updateTimeLe;// 最后修改时间

    private Date addTimeGt;// 新增时间

    private Date addTimeLe;// 新增时间
}
