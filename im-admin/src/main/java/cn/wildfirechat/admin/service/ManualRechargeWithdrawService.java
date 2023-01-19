package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberBalanceLogMapper;
import cn.wildfirechat.admin.mapper.MemberBalanceMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.admin.mapper.WithdrawOrderMapper;
import cn.wildfirechat.common.model.bo.MemberBalanceBO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.CurrencyEnum;
import cn.wildfirechat.common.model.enums.MemberBalanceLogTypeEnum;
import cn.wildfirechat.common.model.po.MemberBalanceLogPO;
import cn.wildfirechat.common.model.po.MemberBalancePO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.po.WithdrawOrderPO;
import cn.wildfirechat.common.model.query.MemberBalanceQuery;
import cn.wildfirechat.common.model.query.WithdrawOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ManualRechargeWithdrawService {
    @Resource
    private MemberBalanceMapper memberBalanceMapper;

    @Resource
    private WithdrawOrderMapper withdrawOrderMapper;

    @Resource
    private MemberBalanceLogMapper memberBalanceLogMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private LogService logService;
    @Transactional(rollbackFor = Exception.class)
    public void rechargeWithdraw(MemberBalanceBO bo) {
        bo.verify();

        CurrencyEnum currencyEnum = CurrencyEnum.CNY;

        MemberBalanceQuery query = MemberBalanceQuery.builder()
                .userId(bo.getUserId())
                .currency(currencyEnum.name())
                .build();
        MemberBalancePO balanceBO = memberBalanceMapper.list(query).stream().findFirst().orElse(null);
        log.info("balanceBO: {}", balanceBO);

        MemberBalanceLogTypeEnum typeEnum = MemberBalanceLogTypeEnum.parse(bo.getType());
        OperateLogList list = new OperateLogList();
        MemberPO memberPO = memberMapper.selectById(bo.getUserId());
        list.addLog("账号", memberPO.getMemberName(), false);
        switch (typeEnum) {
            case MANUAL_RECHARGE:
                list.addLog("手动充值", "充值金额(" + bo.getAmount() + ")", false);
                break;
            case MANUAL_WITHDRAW:
                //需檢查該用戶是否存在其他待審核的提現訂單
                //手動提現最高金額 = 該用戶余額 - 該用戶凍結金額(所有待審核的提現訂單的總額)
                BigDecimal manualWithdrawMaxAmount = balanceBO.getBalance().subtract(balanceBO.getFreeze());
                Assert.isTrue(manualWithdrawMaxAmount.compareTo(bo.getAmount()) >0, "手动提现金额超出余额");
                list.addLog("手动提取", "提取金额(" + bo.getAmount() + ")", false);
                break;
        }


        addBalanceLog(balanceBO, currencyEnum, bo);
        changeBalance(balanceBO, currencyEnum, bo);
        logService.addOperateLog("/admin/manual/rechargeWithdraw", list);
    }

    private void addBalanceLog(MemberBalancePO balanceBO, CurrencyEnum currencyEnum, MemberBalanceBO bo) {
        MemberBalanceLogTypeEnum typeEnum = MemberBalanceLogTypeEnum.parse(bo.getType());

        BigDecimal amount = bo.getAmount() != null ? bo.getAmount() : BigDecimal.ZERO;
        BigDecimal freeze = bo.getFreeze() != null ? bo.getFreeze() : BigDecimal.ZERO;

        BigDecimal beforeBalance;
        BigDecimal afterBalance;
        BigDecimal beforeFreeze;
        BigDecimal afterFreeze;
        if (balanceBO != null) {
            beforeBalance = balanceBO.getBalance();
            beforeFreeze = balanceBO.getFreeze();

            switch (typeEnum) {
                case MANUAL_WITHDRAW:
                    afterBalance = beforeBalance.subtract(amount);
                    afterFreeze = beforeFreeze.subtract(freeze);
                    break;
                default:
                    afterBalance = beforeBalance.add(amount);
                    afterFreeze = beforeFreeze.add(freeze);
                    break;
            }
        } else {
            beforeBalance = BigDecimal.ZERO;
            beforeFreeze = BigDecimal.ZERO;
            afterBalance = amount;
            afterFreeze = freeze;
        }

        String memo = null;
        switch (typeEnum) {
            case MANUAL_RECHARGE:
                if (bo.getFreeze() != null && bo.getFreeze().compareTo(BigDecimal.ZERO) > 0) {
                    memo = MemberBalanceLogPO.MANUAL_RECHARGE_FREEZE
                            .replace("{amount}", amount.toPlainString())
                            .replace("{freeze}", freeze.toPlainString());
                } else {
                    memo = MemberBalanceLogPO.MANUAL_RECHARGE.replace("{amount}", amount.toPlainString());
                }
                break;
            case MANUAL_WITHDRAW:
                if (bo.getFreeze() != null && bo.getFreeze().compareTo(BigDecimal.ZERO) > 0) {
                    memo = MemberBalanceLogPO.MANUAL_WITHDRAW_FREEZE
                            .replace("{amount}", amount.toPlainString())
                            .replace("{freeze}", freeze.toPlainString());
                } else {
                    memo = MemberBalanceLogPO.MANUAL_WITHDRAW.replace("{amount}", amount.toPlainString());
                }
                break;
        }

        MemberBalanceLogPO logPO = MemberBalanceLogPO.builder()
                .userId(bo.getUserId())
                .currency(currencyEnum.name())
                .type(typeEnum.getValue())
                .amount(amount)
                .beforeBalance(beforeBalance)
                .afterBalance(afterBalance)
                .beforeFreeze(beforeFreeze)
                .afterFreeze(afterFreeze)
                .memo(memo)
                .build();
        memberBalanceLogMapper.insert(logPO);
    }

    private void changeBalance(MemberBalancePO balanceBO, CurrencyEnum currencyEnum, MemberBalanceBO bo) {
        BigDecimal amount = bo.getAmount() != null ? bo.getAmount() : BigDecimal.ZERO;
        BigDecimal freeze = bo.getFreeze() != null ? bo.getFreeze() : BigDecimal.ZERO;

        MemberBalanceLogTypeEnum typeEnum = MemberBalanceLogTypeEnum.parse(bo.getType());
        if (balanceBO != null) {
            MemberBalancePO po = MemberBalancePO.builder()
                    .id(balanceBO.getId())
                    .userId(bo.getUserId())
                    .currency(currencyEnum.name())
                    .balance(amount)
                    .freeze(freeze)
                    .build();

            int balCount = 0;
            switch (typeEnum) {
                case MANUAL_RECHARGE:
                    balCount = memberBalanceMapper.addBalance(po);
                    break;
                case MANUAL_WITHDRAW:
                    balCount = memberBalanceMapper.subBalance(po);
                    break;
            }
            Assert.isTrue(balCount > 0, "member balance change error");
        } else {
            MemberBalancePO po = MemberBalancePO.builder()
                    .userId(bo.getUserId())
                    .currency(currencyEnum.name())
                    .balance(amount)
                    .freeze(freeze)
                    .build();
            int balCount = memberBalanceMapper.insert(po);
            Assert.isTrue(balCount > 0, "member balance add error");
        }
    }
}
