package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.bo.AdminUserBO;
import cn.wildfirechat.common.model.dto.AdminUserAuthInfoDTO;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.query.AdminUserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AdminUserMapper {

    List<AdminUserPO> list(AdminUserQuery query);

    AdminUserPO selectById(@Param("id") Long id);

    AdminUserPO selectByMemberId(@Param("memberId") Long memberId);

    AdminUserAuthInfoDTO getAuthInfoByUsername(String username);

    int insert(AdminUserPO po);

    int update(AdminUserPO po);

    int updateLoginInfo(AdminUserPO po);

    int updateFullPath(@Param("id") Long id, @Param("fullPath") String fullPath);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateOtpKey(AdminUserPO po);

    List<AdminUserPO> selectAdminUserByIds(@Param("list") Collection<Long> list);
}
