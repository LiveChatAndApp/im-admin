package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberBalanceLogMapper;
import cn.wildfirechat.admin.mapper.MemberBalanceMapper;
import cn.wildfirechat.admin.mapper.RechargeChannelMapper;
import cn.wildfirechat.admin.mapper.RechargeOrderMapper;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.bo.RechargeOrderBO;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.RechargeChannelInfoDTO;
import cn.wildfirechat.common.model.dto.RechargeOrderDTO;
import cn.wildfirechat.common.model.enums.*;
import cn.wildfirechat.common.model.form.RechargeChannelAddForm;
import cn.wildfirechat.common.model.po.MemberBalanceLogPO;
import cn.wildfirechat.common.model.po.MemberBalancePO;
import cn.wildfirechat.common.model.po.RechargeChannelPO;
import cn.wildfirechat.common.model.po.RechargeOrderPO;
import cn.wildfirechat.common.model.query.MemberBalanceQuery;
import cn.wildfirechat.common.model.query.RechargeChannelQuery;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.support.SpringMessage;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.fastjson2.JSONObject;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RechargeChannelService extends BaseService{
    @Autowired
    private SpringMessage message;

    @Resource
    private RechargeChannelMapper rechargeChannelMapper;


    @Transactional(readOnly = true)
    public PageVO<RechargeChannelPO> page(RechargeChannelQuery query, Page page) {
        page.startPageHelper();
        List<RechargeChannelPO> pos = rechargeChannelMapper.list(query);
        return new PageInfo<>(pos).convertToPageVO();
    }

    public boolean insert(RechargeChannelPO po) {
        int insert = rechargeChannelMapper.insert(po);
        return insert > 0;
    }

    public boolean update(RechargeChannelPO po) {
        int update = rechargeChannelMapper.update(po);
        return update > 0;
    }

    public boolean delete(Long id) {
        int delete = rechargeChannelMapper.delete(id);
        return delete > 0;
    }


    public void addLog(RechargeChannelAddForm form ){
        //充值渠道-添加
        int paymentMethod = form.getPaymentMethod();
        OperateLogList list = new OperateLogList();
        list.addLog("渠道名称",form.getName(),false);
        list.addLog("收款方式", RechargeChannelEnum.parse(paymentMethod).getMessage(),false);
        list.addLog("姓名",form.getRealName(),false);
        if(paymentMethod == 1){
            list.addLog("银行名称(银行卡)",form.getBankName(),false);
            list.addLog("银行账号(银行卡)",form.getBankAccount(),false);
        }else if(paymentMethod == 3){
            list.addLog("账号(支付宝)", form.getBankAccount(),false);
        }
        list.addLog("状态", RechargeChannelStatusEnum.parse(form.getStatus()).getMessage(),false);
        if(StringUtil.isNotBlank(form.getMemo())){
            list.addLog("备注",form.getMemo(),false);
        }
        logService.addOperateLog("/admin/rechargeChannel/add",list);
    }


    public void updateLog(RechargeChannelPO po){
        //充值渠道-编辑 log
        RechargeChannelPO beforePo = rechargeChannelMapper.selectById(po.getId());
            OperateLogList list = new OperateLogList();
            list.addDiffLog("渠道名称",beforePo.getName(),po.getName(),false);
            list.addLog("收款方式",RechargeChannelEnum.parse(beforePo.getPaymentMethod()).getMessage(),false);
            list.addDiffLog("状态",RechargeChannelStatusEnum.parse(beforePo.getStatus()).getMessage(),
                    RechargeChannelStatusEnum.parse(po.getStatus()).getMessage(),false);
            list.addDiffLog("备注",beforePo.getMemo(),po.getMemo(),false);
            logService.addOperateLog("/admin/rechargeChannel/update",list);
    }

    public void deleteLog(Long id){
        RechargeChannelPO po = rechargeChannelMapper.selectById(id);
        String dtoStr = po.getInfo();
        JSONObject dto = JSONObject.parseObject(dtoStr);
        OperateLogList list = new OperateLogList();
        int paymentMethod = po.getPaymentMethod();
        list.addLog("渠道名称",po.getName(),false);
        list.addLog("收款方式",RechargeChannelEnum.parse(paymentMethod).getMessage(),false);
        list.addLog("姓名",dto.get("realName"),false);
        // 收款方式 1:线下支付,2:微信,3:支付宝
        if(paymentMethod == 1) {
            list.addLog("银行名称(银行卡)", dto.get("bankName"), false);
            list.addLog("银行账号(银行卡)", dto.get("bankAccount"), false);
        }
        if(paymentMethod == 3) {
            list.addLog("账号(支付宝)", dto.get("bankAccount"), false);
        }
        list.addLog("状态",RechargeChannelStatusEnum.parse(po.getStatus()).getMessage(),false);
        list.addLog("备注",po.getMemo(),false);
        logService.addOperateLog("/admin/rechargeChannel/delete",list);
    }


}
