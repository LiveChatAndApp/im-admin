package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Business Object 会员加好友业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinFriendBO {

	public static Integer ADD_FRIEND = 0;
	public static Integer DELETE_FRIEND = 1;
	public static Integer BLOCK_FRIEND = 2;

	private Long selfMemberId;// 会员流水号
	private Long memberId;// 添加对象会员流水号
	private String selfUid;// uid
	private String targetUid;// 目标uid
	private Integer verify;// 验证 0: 待同意, 1: 需要验证讯息, 2: 成功, 3: 失败, 4: 已删除 [RelateVerifyEnum]
	private String verifyText;// 验证消息
	private Integer action; // IMServer传的值
	private Date addTime; // 加入时间
	private Integer blacked;// 黑名单 0:正常,1:拉黑
}
