package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.GroupMemberPO;
import cn.wildfirechat.common.model.query.GroupMemberQuery;
import cn.wildfirechat.common.model.vo.GroupMemberPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMemberMapper {

    Integer inserts(List<GroupMemberPO> groupMemberPO, Integer memberType);

    Integer update(GroupMemberPO groupMemberPO);

    Integer updates(@Param("idList") List<Long> idList, Long groupId, Integer memberType);

    List<GroupMemberPO> list(GroupMemberQuery groupMemberQuery);

    List<GroupMemberPageVO> selectGroupMemberPageVO(GroupMemberQuery groupMemberQuery);

    List<GroupMemberPageVO> selectWithoutGroupMemberPageVO(GroupMemberQuery groupMemberQuery);

    Integer delete(Long groupId, List<Long> memberIdList);

    Integer insertUpdate(GroupMemberPO po);
}
