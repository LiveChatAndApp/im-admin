package cn.wildfirechat.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthTreeVO extends TreeNode {

    private String code;

    private String name ;

    private String api;

    private Boolean isLog;
}
