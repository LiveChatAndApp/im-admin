package cn.wildfirechat.common.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Business Object 群组會員同步业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class GroupMemberSyncBO {
    private Integer behavior;
    private List<Member> members;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties
    public static class Member {
        private String gid;
        private String mid;
        private String alias;
        private Integer type;// 0普通成员,1:管理员,2:群主与Owner相同
        private Long dt;
        private String extra;
    }
}
