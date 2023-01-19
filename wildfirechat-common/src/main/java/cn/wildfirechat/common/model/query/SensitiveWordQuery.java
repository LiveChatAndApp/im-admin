package cn.wildfirechat.common.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query Object 敏感词查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordQuery {
    private String content;// 敏感词内容
//    private String createTimeGt;// 创建起始时间
//    private String createTimeLe;// 创建结束时间
}
