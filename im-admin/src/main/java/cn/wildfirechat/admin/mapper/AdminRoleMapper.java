package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.AdminRoleInfoPO;
import cn.wildfirechat.common.model.po.AdminRolePO;
import cn.wildfirechat.common.model.query.AdminRoleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface AdminRoleMapper {

    AdminRoleInfoPO selectRoleInfoByUsername(@Param("username") String username);

    AdminRolePO selectById(@Param("id") Long id);

    List<AdminRolePO> list(AdminRoleQuery query);

    boolean insert(AdminRolePO adminRolePO);

    boolean update(AdminRolePO adminRolePO);
}
