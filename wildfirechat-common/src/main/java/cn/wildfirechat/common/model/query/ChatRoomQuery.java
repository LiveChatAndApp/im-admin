package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Query Object 查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomQuery {
    private Long id;// ID
    private String cid;// 聊天室ID
    private String name;
    private Integer sort;
    private Integer status;
    private String createTimeGt;// 创建时间
    private String createTimeLe;// 创建时间
}
