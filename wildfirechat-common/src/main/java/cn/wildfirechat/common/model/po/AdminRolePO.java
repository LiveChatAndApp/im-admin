package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Persistant Object 管理员角色
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRolePO {
    private Long id;// ID
    private String name;// 角色名称
    private Integer level;// 级别 1:超级管理员, 2: 普通管理员[[AdminRoleLevelEnum]]
    private String brandName;// 品牌
    private String memo;// 备注
    private Date createTime;// 创建时间
    private Date updateTime;// 最后修改时间
}
