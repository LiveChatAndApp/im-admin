package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Persistant Object 管理员权限
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthPO implements Serializable {
    private Long id; // ID
    private Long parentId; // 父层ID
    private String code; // 权限代码
    private String name; // 权限名称
    private Integer type; // 类型
    private String api; // api Url
    private Boolean isLog; // 是否寫log,0-不寫入,1-寫入
    private Integer order; // 排序
    private Integer version; // 版本
}
