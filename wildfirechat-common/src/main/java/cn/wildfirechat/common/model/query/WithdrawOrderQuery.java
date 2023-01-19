package cn.wildfirechat.common.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Query Object 提现订单查询对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawOrderQuery {
    private Long id;
    private String orderCode;// 订单偏号
    private Long userId;// 用户ID
    private String currency;// 交易货币
    private Integer channel;// 提现渠道 1:银行卡,2:游戏平台
    private Integer status;// 状态 1:待审核,2:已完成,3:已拒绝

    private String memberName;// 用户帐号
    private Date createTimeGt;// 提交时间起
    private Date createTimeLe;// 提交时间迄
    private Date completeTimeGt;// 完成时间起
    private Date completeTimeLe;// 完成时间迄
}
