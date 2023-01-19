package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.MemberBalanceLogDTO;
import cn.wildfirechat.common.model.po.MemberBalanceLogPO;
import cn.wildfirechat.common.model.query.MemberBalanceLogQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberBalanceLogMapper {
    List<MemberBalanceLogPO> list(MemberBalanceLogQuery query);
    List<MemberBalanceLogDTO> selectFundDetails(MemberBalanceLogQuery query);
    int insert(MemberBalanceLogPO po);
}
