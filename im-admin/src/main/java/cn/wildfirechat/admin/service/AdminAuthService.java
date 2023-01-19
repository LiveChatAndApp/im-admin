package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.AdminAuthMapper;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AdminAuthService extends BaseService {
    @Resource
    private AdminAuthMapper adminAuthMapper;

    @Transactional(readOnly = true)
    public List<AdminAuthPO> list(AdminAuthQuery query) {
        return adminAuthMapper.list(query);
    }

    @Transactional(readOnly = true)
    public List<AdminAuthPO> getByRoleId(Long roleId) {
        return adminAuthMapper.selectAuthByRoles(roleId);
    }
}
