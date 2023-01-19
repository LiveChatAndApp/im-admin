package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistant Object 会员列表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordPO {

	private String uid;//用户UID

	private String password;//密码

	private String salt;//加盐

	private String resetCode;//重置码

	private long resetCodeTime;//发送重置码时间

	private int tryCount;//登入(失败)次数

	private long lastTryTime;//尝试登入时间


	public MemberPasswordPO(String uid) {
		this.uid = uid;
	}

	public MemberPasswordPO(String uid, String password, String salt) {
		this.uid = uid;
		this.password = password;
		this.salt = salt;
		this.resetCodeTime = 0;
		this.tryCount = 0;
		this.lastTryTime = 0;
	}

	public MemberPasswordPO(String uid, String password, String salt, String resetCode, long resetCodeTime) {
		this.uid = uid;
		this.password = password;
		this.salt = salt;
		this.resetCode = resetCode;
		this.resetCodeTime = resetCodeTime;
		this.tryCount = 0;
		this.lastTryTime = 0;
	}

}
