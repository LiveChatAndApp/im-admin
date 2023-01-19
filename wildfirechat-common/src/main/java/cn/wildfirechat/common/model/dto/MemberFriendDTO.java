package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object 会員聊天列表数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFriendDTO {

	private Long id; // ID

	private String uid; // UID

	private String nickName; // 昵称

	private String memberName; // 账号

	private String gender; // 性别

	private Date addTime; // 添加好友時間
}
