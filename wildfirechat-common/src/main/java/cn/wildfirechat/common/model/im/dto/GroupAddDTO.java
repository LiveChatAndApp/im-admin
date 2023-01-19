package cn.wildfirechat.common.model.im.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class GroupAddDTO {
    private String creator;// 创建者
    private Integer creatorRole;// 创建者角色 1: 系统管理者, 2: 会员
    private Integer groupType;// 群组类型 1:一般,2:广播
}
