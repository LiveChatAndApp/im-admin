package cn.wildfirechat.common.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Query Object 会员查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOperateLogQuery {
    private String uid;// UID
    private String memberName;// 帐号
//    private List<Long> authIds;// 多请求方法
    private Long authId;// 请求方法
    private String creatorIp;// IP地址
    private String createTimeGt;// 创建起始时间
    private String createTimeLe;// 创建结束时间
}
