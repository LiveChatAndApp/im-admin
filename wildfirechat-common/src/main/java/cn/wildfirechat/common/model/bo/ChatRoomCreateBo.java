package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 聊天室新增业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateBo {
    private String name;
    private Integer sort;
    private String image;
}
