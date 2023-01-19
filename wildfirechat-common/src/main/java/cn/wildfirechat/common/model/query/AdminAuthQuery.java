package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query Object 系统授权查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthQuery {
    private Long id; // ID
    private Long parentId; // 父层ID
    private String code; // 权限代码
    private Integer type; // 类型
    private String api; // api Url
    private Boolean isLog; // 是否寫log,0-不寫入,1-寫入
    private Integer version; // 版本
}
