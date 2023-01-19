package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberBalanceLogMapper;
import cn.wildfirechat.admin.mapper.MemberBalanceMapper;
import cn.wildfirechat.admin.mapper.RechargeOrderMapper;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.RechargeOrderBO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import cn.wildfirechat.common.model.dto.RechargeOrderDTO;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.po.MemberBalanceLogPO;
import cn.wildfirechat.common.model.po.MemberBalancePO;
import cn.wildfirechat.common.model.po.RechargeOrderPO;
import cn.wildfirechat.common.model.query.MemberBalanceQuery;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.common.utils.DateUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RechargeService extends BaseService {
    @Autowired
    private SpringMessage message;

    @Resource
    private RechargeOrderMapper rechargeOrderMapper;

    @Resource
    private MemberBalanceMapper memberBalanceMapper;

    @Resource
    private MemberBalanceLogMapper memberBalanceLogMapper;

    @Transactional(readOnly = true)
    public PageVO<RechargeOrderDTO> page(RechargeOrderQuery query, Page page) {
        page.startPageHelper();
        List<RechargeOrderDTO> dtos = rechargeOrderMapper.selectRechargeOrders(query);
        return new PageInfo<>(dtos).convertToPageVO();
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(RechargeOrderBO bo) {
        RechargeOrderPO orderPO = rechargeOrderMapper.selectById(bo.getId());
        int beforeStatus =  orderPO.getStatus();
        Assert.notNull(orderPO, message.getMessage(I18nAdmin.RECHARGE_ORDER_NOT_EXIST));
        BigDecimal amount = orderPO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        Assert.isTrue(RechargeOrderStatusEnum.isAudit(orderPO.getStatus()), message.getMessage(I18nAdmin.RECHARGE_ORDER_STATUS_NOT_SUPPORT));

        CurrencyEnum currencyEnum = CurrencyEnum.parse(orderPO.getCurrency());
//        CurrencyEnum currencyEnum = CurrencyEnum.CNY;
        MemberBalanceQuery balanceQuery = MemberBalanceQuery.builder()
                .userId(orderPO.getUserId())
                .currency(currencyEnum.name())
                .build();
        List<MemberBalancePO> balancePOs = memberBalanceMapper.list(balanceQuery);
        MemberBalancePO balancePO = balancePOs.stream().findFirst().orElse(null);

        RechargeOrderStatusEnum statusEnum = RechargeOrderStatusEnum.parse(bo.getStatus());

        RechargeOrderPO updateOrderPO = RechargeOrderPO.builder()
                .id(orderPO.getId())
                .status(statusEnum.getValue())
                .completeTime(new Date())
                .updaterId(bo.getUpdaterId())
                .updaterRole(MessageSenderRoleEnum.ADMIN.getValue())
                .updateTime(new Date())
                .build();
        orderPO.setStatus(statusEnum.getValue());
        rechargeOrderMapper.update(updateOrderPO);

        if (RechargeOrderStatusEnum.COMPLETED.equals(statusEnum)) {
            BigDecimal beforeBalance = BigDecimal.ZERO;
            BigDecimal beforeFreeze = BigDecimal.ZERO;
            if (balancePO != null) {
                beforeBalance = balancePO.getBalance();
                beforeFreeze = balancePO.getFreeze();

                MemberBalancePO po = MemberBalancePO.builder()
                        .id(balancePO.getId())
                        .balance(amount)
                        .freeze(BigDecimal.ZERO)
                        .build();
                memberBalanceMapper.addBalance(po);
            } else {
                balancePO = MemberBalancePO.builder()
                        .userId(orderPO.getUserId())
                        .currency(currencyEnum.name())
                        .balance(orderPO.getAmount())
                        .freeze(BigDecimal.ZERO)
                        .build();
                memberBalanceMapper.insert(balancePO);
            }

            MemberBalanceLogPO logPO = MemberBalanceLogPO.builder()
                    .userId(orderPO.getUserId())
                    .currency(currencyEnum.name())
                    .type(MemberBalanceLogTypeEnum.RECHARGE.getValue())
                    .amount(amount)
                    .beforeBalance(beforeBalance)
                    .afterBalance(beforeBalance.add(orderPO.getAmount()))
                    .beforeFreeze(beforeFreeze)
                    .afterFreeze(beforeFreeze)
                    .memo(MemberBalanceLogPO.USER_RECHARGE.replace("{amount}", amount.toPlainString()))
                    .build();
            memberBalanceLogMapper.insert(logPO);

            //充值订单-审核 log
            OperateLogList list = new OperateLogList();
            list.addLog("订单编号",orderPO.getOrderCode(),false);
            list.addDiffLog("订单状态",RechargeOrderStatusEnum.parse(beforeStatus).getMessage(),
                    RechargeOrderStatusEnum.parse(bo.getStatus()).getMessage(),false);
            logService.addOperateLog("/admin/rechargeOrder/audit",list);
        }
    }

    public List<OperateTotalDTO> totalByDate(Date date){
        return rechargeOrderMapper.totalByDate(DateUtils.format(date, "yyyy-MM-dd"));
    }
}
