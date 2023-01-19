package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.query.GroupPageQuery;
import cn.wildfirechat.common.model.query.GroupQuery;
import cn.wildfirechat.common.model.vo.GroupPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface GroupMapper {

    List<GroupPO> list(GroupQuery groupQuery);

    List<GroupPageVO> selectGroupPageVO(GroupPageQuery groupPageQuery);

    List<GroupPO> selectGroupByIds(@Param("list") Collection<Long> list);

    GroupPO selectByGid(@Param("gid") String gid);

    GroupPO selectById(@Param("id") Long id);

    Integer insert(GroupPO groupPO);

    Integer insertUpdate(GroupPO groupPO);

    Long update(GroupPO groupPO);

    Integer delete(Long groupId);

    Integer count();

    Integer countCreateAtCurrDate();
}
