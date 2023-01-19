package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import cn.wildfirechat.common.model.dto.WithdrawOrderDTO;
import cn.wildfirechat.common.model.po.WithdrawOrderPO;
import cn.wildfirechat.common.model.query.WithdrawOrderQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WithdrawOrderMapper {
    WithdrawOrderPO selectById(Long id);
    List<WithdrawOrderPO> list(WithdrawOrderQuery query);
    List<WithdrawOrderDTO> selectWithdrawOrders(WithdrawOrderQuery query);
    int update(WithdrawOrderPO po);

    List<OperateTotalDTO> totalByDate(String date);
}
