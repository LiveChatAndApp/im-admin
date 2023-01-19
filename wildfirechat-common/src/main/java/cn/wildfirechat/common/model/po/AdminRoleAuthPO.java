package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleAuthPO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long roleId;
    private Long authId;
    private Integer version;

    public AdminRoleAuthPO(Long roleId, Long authId) {
        super();
        this.roleId = roleId;
        this.authId = authId;
    }

}
