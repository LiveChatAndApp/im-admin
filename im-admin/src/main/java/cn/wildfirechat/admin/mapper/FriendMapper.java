package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.FriendPO;
import cn.wildfirechat.common.model.query.FriendQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface FriendMapper {

	Integer insert(FriendPO friendPO);

	Integer update(FriendPO friendPO);

	List<FriendPO> list(FriendQuery friendQuery);

	List<FriendPO> relateExist(Long id, Long memberSourceId, Long memberTargetId);

	List<FriendPO> selectFriendRelate(Long memberid, Integer verify, @Param("memberIdList") Collection<Long> memberIdList);

	Integer delete(Long id);
}
