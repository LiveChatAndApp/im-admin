package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 群
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomPO implements Serializable {
    private Long id;// ID
    private String cid;// 聊天室ID
    private String name;// 群名称(群信息)
    private Integer sort;// 排序
    private String image;// 图挡路径
    private Integer status;// 状态
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
    private Integer chatStatus;//发言状态
    private String desc;//聊天室详情描述
    private String extra;//附加信息
}
