package cn.wildfirechat.common.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 群组同步业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class GroupSyncBO {
    private String gid;
    private String name;
    private String portrait;
    private String owner;
    private Integer type;
    private String extra;
    private Long dt;
    private Integer memberCount;
    private Integer mute;// 全员禁言 0:否,1:是
    private Integer joinType;// 加群方式: 0:不限制加入,1:群成员可以拉人,2:只能群管理拉人
    private Integer privateChat;// 普通成员发起临时会话: 1:不允许,0:允许
    private Integer searchable;// 寻找方式: 0:不允许,1:允许
    private Integer historyMessage;// 查看历史讯息 0:不允许,1:允许
    private Long memberDt;
    private Integer status;// 状态 1: 正常, 2: 解散
}
