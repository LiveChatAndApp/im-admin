package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.AdminRoleAuthPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleAuthMapper {

    int countByRoleIdInt(@Param("roleId") Long roleId);

    int insert(AdminRoleAuthPO auth);

    int batchInsert(List<AdminRoleAuthPO> auths);

    int delete(Long id);

    int deleteByRoleId(@Param("roleId") Long roleId);



//    int batchDeleteByRoleAndAuthId(@Param("roleId") Long roleId, @Param("ids") List<Long> ids);
//
//    int countRoleAuthByRoleIdAndAuthId(AdminRoleAuthPO auth);

}
