package cn.wildfirechat.common.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordPO {
	/** 流水号 */
    private Long id;
    /** 敏感词内容 */
    private String content;
    /** 创建者帐号 */
    private String creator;
    /** 创建时间 */
    private Date createTime;
    /** 创建者帐号 */
    private String updater;
    /** 创建时间 */
    private Date updateTime;

}
