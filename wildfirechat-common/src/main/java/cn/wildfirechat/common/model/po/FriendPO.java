package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 朋友持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendPO implements Serializable {

    private Long id;// 朋友流水号

    private Long memberSourceId;// 邀请者ID

    private Long memberTargetId; // 接受者ID

    private Integer verify; // 验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除 [RelateVerifyEnum]

    private String verifyText; // 验证消息

    private Long requestReceiver; // 好友邀请发送者ID

    private Integer sourceBlacked;// 邀请者黑名单 1:一般,2：拉黑

    private Integer targetBlacked;// 接受者黑名单 1:一般,2：拉黑

    private Date createTime;// 创建时间

    private Date updateTime;// 最后修改时间

    private Date addTime;// 新增时间
}
