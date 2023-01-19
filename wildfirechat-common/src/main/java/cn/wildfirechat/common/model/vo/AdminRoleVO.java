package cn.wildfirechat.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * View Object 系统管理者显示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleVO {
    private Long id;
    private String name;// 角色名称
    private Integer level;// 级别 1:超级管理员, 2: 普通管理员[[AdminRoleLevelEnum]]
    private String memo;// 备注
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
}
