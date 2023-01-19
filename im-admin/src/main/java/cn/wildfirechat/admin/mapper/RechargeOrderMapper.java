package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import cn.wildfirechat.common.model.dto.RechargeOrderDTO;
import cn.wildfirechat.common.model.po.RechargeOrderPO;
import cn.wildfirechat.common.model.po.WithdrawOrderPO;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RechargeOrderMapper {

    RechargeOrderPO selectById(Long id);

    List<RechargeOrderPO> list(RechargeOrderQuery query);

    List<RechargeOrderDTO> selectRechargeOrders(RechargeOrderQuery query);

    int update(RechargeOrderPO po);

    List<OperateTotalDTO> totalByDate(String date);
}
