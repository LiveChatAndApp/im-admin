package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.query.MemberQuery;
import cn.wildfirechat.common.model.vo.MemberFriendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MemberMapper {
    List<MemberPO> list(MemberQuery query);

    MemberPO selectByIdNamePhone(MemberQuery query);

    MemberPO selectById(@Param("id") Long id);

    MemberPO selectByUid(@Param("uid") String uid);

    List<MemberPO> selectMemberByIds(@Param("list") Collection<Long> idList);

    List<MemberPO> selectMemberByUids(@Param("list") List<String> ids);

    int insert(MemberPO po);

    Integer inserts(List<MemberPO> list);

    int update(MemberPO memberPO);

    int updateUid(MemberPO memberPO);

    int batchUpdateLoginAndCreateGroup(MemberPO memberPO);

    List<MemberPO> selectMemberByMemberNameOrPhone(String member);

    Integer count();

    Integer countCreateAtCurrDate();
}
