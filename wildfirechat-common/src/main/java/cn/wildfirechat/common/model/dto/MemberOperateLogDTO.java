package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOperateLogDTO {
	/** 流水号 */
    private Long id;
    /** 用戶UID编号 */
    private String uid;
    /** 用戶名 */
    private String memberName;
    /** 用戶昵称 */
    private String nickName;
    /** 用戶头像 */
    private String avatarUrl;
    /** 手机号 */
    private String phone;
    /** 备注 */
    private String memo;
    /** 请求方法(t_admin_auth.id) */
    private Long authId;
    /** 创建时间 */
    private Date createTime;
    /** 创建者Level */
    private Integer creatorLevel;
    /** 创建者帐号 */
    private String creator;
    /** 创建者IP */
    private String creatorIp;
    /** 创建者位置 */
    private String creatorLocation;
}
