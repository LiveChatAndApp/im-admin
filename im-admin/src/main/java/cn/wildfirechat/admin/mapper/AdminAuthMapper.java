package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface AdminAuthMapper {

    List<AdminAuthPO> list(AdminAuthQuery query);

    List<AdminAuthPO> selectAuthByRoles(@RequestParam("roleId") Long roleId);

    int insert(AdminAuthPO po);
}
