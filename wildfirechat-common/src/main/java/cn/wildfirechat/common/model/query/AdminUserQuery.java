package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query Object 系统管理者查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserQuery {

    private Long id;//后台用户ID

    private String username;//后台用户名

    private String chatUserName; // 聊天帐号

    private Long chatId; // 聊天帐号ID

    private Integer status;//状态

    private Integer level;

    private Long memberId;
}
