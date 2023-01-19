package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.MemberBalancePO;
import cn.wildfirechat.common.model.query.MemberBalanceQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberBalanceMapper {
    List<MemberBalancePO> list(MemberBalanceQuery query);
    int insert(MemberBalancePO po);
    int addBalance(MemberBalancePO po);
    int subBalance(MemberBalancePO po);
    int rejectBalance(MemberBalancePO po);
}
