package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.RechargeOrderDTO;
import cn.wildfirechat.common.model.po.RechargeChannelPO;
import cn.wildfirechat.common.model.po.RechargeOrderPO;
import cn.wildfirechat.common.model.query.RechargeChannelQuery;
import cn.wildfirechat.common.model.query.RechargeOrderQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RechargeChannelMapper {

    int insert(RechargeChannelPO query);

    int update(RechargeChannelPO po);

    int delete(Long id);

    RechargeChannelPO selectById(Long id);

    List<RechargeChannelPO> list(RechargeChannelQuery query);
}
