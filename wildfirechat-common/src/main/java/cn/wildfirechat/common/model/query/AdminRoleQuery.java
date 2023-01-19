package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleQuery {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Date createTimeGe;

    private Date createTimeLe;

    private Integer level;


}
