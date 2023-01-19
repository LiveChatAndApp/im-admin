package cn.wildfirechat.admin.job;

import cn.wildfirechat.admin.mapper.RechargeOrderMapper;
import cn.wildfirechat.common.model.enums.MessageSenderRoleEnum;
import cn.wildfirechat.common.model.enums.RechargeOrderStatusEnum;
import cn.wildfirechat.common.model.po.RechargeOrderPO;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RechargeOrderStatusJob {

    private static final String logPrefix = "【排程更新订单状态】";

    private static final long TEN_MINUTES = 1000 * 60 * 10;

    @Resource
    private RechargeOrderMapper rechargeOrderMapper;

    // 每隔1分鐘檢查 , 检查订单状态(超过10分钟状态未变时 , 订单状态变更为 "订单超时")
    @Scheduled(fixedRate = 1 * TimeUnit.MINUTE)
    public void updateRechargeOrderStatus() {
        try {
            RechargeOrderQuery query = RechargeOrderQuery.builder()
                    .status(RechargeOrderStatusEnum.CREATE.getValue())
                    .build();
            List<RechargeOrderPO> dtos = rechargeOrderMapper.list(query);
            Date now = new Date();
            dtos.stream()
                    .filter(dto -> (now.getTime() - dto.getCreateTime().getTime()) > TEN_MINUTES)
                    .forEach(dto -> {
                        dto.setStatus(RechargeOrderStatusEnum.TIMEOUT.getValue());
                        RechargeOrderPO po = RechargeOrderPO.builder()
                                .id(dto.getId())
                                .status(dto.getStatus())
                                .completeTime(now)
                                .updaterId(1L) // 系统管理者
                                .updaterRole(MessageSenderRoleEnum.ADMIN.getValue())
                                .updateTime(now)
                                .build();
                        rechargeOrderMapper.update(po);
                    });
        } catch (Exception e) {
            log.info("「Job」{} 发生异常", logPrefix, e);
        }
        log.info("「Job」{} RechargeOrderStatusJob.updateRechargeOrderStatus End", logPrefix);
    }
}
