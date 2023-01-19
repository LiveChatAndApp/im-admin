package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberBalanceLogMapper;
import cn.wildfirechat.admin.mapper.MemberBalanceMapper;
import cn.wildfirechat.admin.mapper.WithdrawOrderMapper;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.WithdrawOrderBO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import cn.wildfirechat.common.model.dto.WithdrawOrderDTO;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.po.MemberBalanceLogPO;
import cn.wildfirechat.common.model.po.MemberBalancePO;
import cn.wildfirechat.common.model.po.WithdrawOrderPO;
import cn.wildfirechat.common.model.query.MemberBalanceQuery;
import cn.wildfirechat.common.model.query.WithdrawOrderQuery;
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
public class WithdrawOrderService extends BaseService {
    @Autowired
    private SpringMessage message;

    @Resource
    private WithdrawOrderMapper withdrawOrderMapper;

    @Resource
    private MemberBalanceMapper memberBalanceMapper;

    @Resource
    private MemberBalanceLogMapper memberBalanceLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void audit(WithdrawOrderBO bo) {
        WithdrawOrderPO orderPO = withdrawOrderMapper.selectById(bo.getId());
        Assert.notNull(orderPO, message.getMessage(I18nAdmin.WITHDRAW_ORDER_NOT_EXIST));

        BigDecimal amount = orderPO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        Assert.isTrue(RechargeOrderStatusEnum.isAudit(orderPO.getStatus()), message.getMessage(I18nAdmin.WITHDRAW_ORDER_STATUS_NOT_SUPPORT));

        CurrencyEnum currencyEnum = CurrencyEnum.CNY;
        MemberBalanceQuery balanceQuery = MemberBalanceQuery.builder()
                .userId(orderPO.getUserId())
                .currency(currencyEnum.name())
                .build();
        List<MemberBalancePO> balancePOs = memberBalanceMapper.list(balanceQuery);
        MemberBalancePO balancePO = balancePOs.stream().findFirst().orElse(null);
        Assert.notNull(balancePO, message.getMessage(I18nAdmin.MEMBER_ASSETS_NOT_EXIST));

        WithdrawOrderStatusEnum statusEnum = WithdrawOrderStatusEnum.parse(bo.getStatus());
        WithdrawOrderPO updateOrderPO = WithdrawOrderPO.builder()
                .id(orderPO.getId())
                .status(statusEnum.getValue())
                .completeTime(new Date())
                .updaterId(bo.getUpdaterId())
                .updaterRole(MessageSenderRoleEnum.ADMIN.getValue())
                .updateTime(new Date())
                .build();
        withdrawOrderMapper.update(updateOrderPO);

        BigDecimal afterBalance = null;
        String memo = null;
        MemberBalanceLogTypeEnum typeEnum = null;
        MemberBalancePO balanceBO = MemberBalancePO.builder()
                .id(balancePO.getId())
                .balance(amount)
                .freeze(amount)
                .build();

        switch (statusEnum) {
            case COMPLETED:
                Integer balCount = memberBalanceMapper.subBalance(balanceBO);
                Assert.isTrue(balCount > 0, message.getMessage(I18nAdmin.MEMBER_ASSETS_INSUFFICIENT));

                memo = MemberBalanceLogPO.USER_WITHDRAW_FREEZE
                        .replace("{amount}", amount.toPlainString())
                        .replace("{freeze}", amount.toPlainString());

                typeEnum = MemberBalanceLogTypeEnum.WITHDRAW;

                afterBalance = balancePO.getBalance().subtract(orderPO.getAmount());
                break;
            case REJECTED:
                memberBalanceMapper.rejectBalance(balanceBO);

                memo = MemberBalanceLogPO.USER_WITHDRAW_REJECT_FREEZE
                        .replace("{amount}", amount.toPlainString())
                        .replace("{freeze}", amount.toPlainString());

                typeEnum = MemberBalanceLogTypeEnum.WITHDRAW_REFUSED;

                afterBalance = balancePO.getBalance();
                break;
        }

        MemberBalanceLogPO logPO = MemberBalanceLogPO.builder()
                .userId(orderPO.getUserId())
                .currency(currencyEnum.name())
                .type(typeEnum.getValue())
                .amount(amount)
                .beforeBalance(balancePO.getBalance())
                .afterBalance(afterBalance)
                .beforeFreeze(balancePO.getFreeze())
                .afterFreeze(balancePO.getFreeze().subtract(orderPO.getAmount()))
                .memo(memo)
                .build();
        memberBalanceLogMapper.insert(logPO);
        //提现订单-审核 log
        OperateLogList list = new OperateLogList();
        list.addLog("订单编号",orderPO.getOrderCode(),false);
        list.addDiffLog("订单状态",WithdrawOrderStatusEnum.parse(orderPO.getStatus()).getMessage(),
                WithdrawOrderStatusEnum.parse(bo.getStatus()).getMessage(),false);
        logService.addOperateLog("/admin/withdrawOrder/audit",list);
    }

    @Transactional(readOnly = true)
    public PageVO<WithdrawOrderDTO> page(WithdrawOrderQuery query, Page page) {
        page.startPageHelper();
        List<WithdrawOrderDTO> dtos = withdrawOrderMapper.selectWithdrawOrders(query);
        return new PageInfo<>(dtos).convertToPageVO();
    }

    public List<OperateTotalDTO> totalByDate(Date date){
        return withdrawOrderMapper.totalByDate(DateUtils.format(date, "yyyy-MM-dd"));
    }
}
