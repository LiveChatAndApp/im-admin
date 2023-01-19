package cn.wildfirechat.common.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultMemberDTO {

    private Long id; // 流水号ID

    private String memberName; // 帐号

    private String nickName; // 昵称

    private String gid; // 群组id

    private String inviteCode; // 邀请码

}
