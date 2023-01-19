package cn.wildfirechat.common.model.vo;

import cn.wildfirechat.common.model.dto.OperateLogMemoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOperateLogVO {
	/** 流水号 */
    private Long id;
    /** 用戶UID编号 */
    private String uid;
    /** 用戶帐号 */
    private String memberName;
    /** 用戶昵称 */
    private String nickName;
    /** 用戶头像 */
    private String avatarUrl;
    /** 手机号 */
    private String phone;
    /** 备注 */
    private OperateLogMemoDto memo;
    /** 请求方法(t_admin_auth.id) */
    private Long authId;
    /** 请求方法(t_admin_auth._name) */
    private String authName;
    /** 创建时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
