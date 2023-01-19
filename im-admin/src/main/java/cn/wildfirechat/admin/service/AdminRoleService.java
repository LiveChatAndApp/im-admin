package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.AdminRoleAuthMapper;
import cn.wildfirechat.admin.mapper.AdminRoleMapper;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.enums.AdminRoleLevelEnum;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.i18n.I18nAdmin;
import cn.wildfirechat.common.model.po.AdminRoleAuthPO;
import cn.wildfirechat.common.model.po.AdminRoleInfoPO;
import cn.wildfirechat.common.model.po.AdminRolePO;
import cn.wildfirechat.common.model.query.AdminRoleQuery;
import cn.wildfirechat.common.model.vo.AdminRoleVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminRoleService extends BaseService {

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Resource
    private AdminRoleAuthMapper adminRoleAuthMapper;

    @Transactional(readOnly = true)
    public AdminRoleInfoPO getRoleInfoByUsername(String username) {
        AdminRoleInfoPO authPo = adminRoleMapper.selectRoleInfoByUsername(username);
        return authPo;
    }


    public PageVO<AdminRoleVO> page(AdminRoleQuery query, Page page){
        page.startPageHelper();
        List<AdminRolePO> list = adminRoleMapper.list(query);
        PageVO<AdminRolePO> AdminRolePOPageVO = new PageInfo<>(list).convertToPageVO();

        List<AdminRoleVO> vos = new ArrayList<>();
        list.forEach(po -> {
            AdminRoleVO dto = AdminRoleVO.builder().build();
            ReflectionUtils.copyFields(dto, po, ReflectionUtils.STRING_TRIM_TO_NULL);
            vos.add(dto);
        });
        PageVO<AdminRoleVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(AdminRolePOPageVO.getTotal());
        pageVO.setTotalPage(AdminRolePOPageVO.getTotalPage());
        return pageVO;
    }

    public AdminRolePO selectById(Long id){
        return adminRoleMapper.selectById(id);
    }

    public Map<Long, AdminRolePO> selectMap(AdminRoleQuery query){
        List<AdminRolePO> list = adminRoleMapper.list(query);
        Map<Long, AdminRolePO> map = list.stream().collect(Collectors.toMap(AdminRolePO::getId, Function.identity()));
        return map;
    }

    public List<AdminRoleVO> list(AdminRoleQuery query){
        List<AdminRolePO> list = adminRoleMapper.list(query);
        List<AdminRoleVO> vos = new ArrayList<>();
        list.forEach(po -> {
            AdminRoleVO dto = AdminRoleVO.builder().build();
            ReflectionUtils.copyFields(dto, po, ReflectionUtils.STRING_TRIM_TO_NULL);
            vos.add(dto);
        });

        return vos;
    }


    @Transactional
    public AdminRolePO insert(AdminRolePO rolePO, List<Long> adminAuthList) {
        Assert.notEmpty(adminAuthList, message.get(I18nAdmin.ROLE_AUTH_LIST_IS_EMPTY));

        boolean result = adminRoleMapper.insert(rolePO);
        Assert.isTrue(result, message.get(I18nAdmin.DB_FAIL));

        //角色权限
        List<AdminRoleAuthPO> authIds = new ArrayList<AdminRoleAuthPO>();
        for(Long adminAuthId : adminAuthList){
            AdminRoleAuthPO roleAuth = AdminRoleAuthPO.builder()
                    .roleId(rolePO.getId())
                    .authId(adminAuthId)
                    .build();
            authIds.add(roleAuth);
        }
        int insetNum = adminRoleAuthMapper.batchInsert(authIds);
        Assert.isTrue(insetNum > 0, message.get(I18nAdmin.DB_FAIL));

        return rolePO;
    }

    @Transactional
    public AdminRolePO update(AdminRolePO rolePO, List<Long> adminAuthList){
        Assert.notEmpty(adminAuthList, message.get(I18nAdmin.ROLE_AUTH_LIST_IS_EMPTY));

        AdminRolePO bfRole = adminRoleMapper.selectById(rolePO.getId());// 撈出 角色配置

        boolean result = adminRoleMapper.update(rolePO);
        Assert.isTrue(result, message.get(I18nAdmin.DB_FAIL));

        Integer permissionsQuantity = adminRoleAuthMapper.countByRoleIdInt(rolePO.getId());
        if (adminAuthList != null) {
            //修改权限
            adminRoleAuthMapper.deleteByRoleId(rolePO.getId());
            List<AdminRoleAuthPO> authIds = new ArrayList<AdminRoleAuthPO>();
            for (Long authId : adminAuthList) {
                AdminRoleAuthPO aclRole = AdminRoleAuthPO.builder()
                        .roleId(rolePO.getId())
                        .authId(authId)
                        .build();
                authIds.add(aclRole);
            }
            int insetNum = adminRoleAuthMapper.batchInsert(authIds);
            Assert.isTrue(insetNum > 0, message.get(I18nAdmin.DB_FAIL));

        }
        //角色-更新 log
        String roleLevel = AdminRoleLevelEnum.parse(bfRole.getLevel()).getName();
        OperateLogList list = new OperateLogList();
        list.addLog("角色名称",rolePO.getName(),false);
        list.addLog("用户层级",roleLevel,false);
        if(adminAuthList.size()>0){
            list.addDiffLog("权限数量",permissionsQuantity,adminAuthList.size(),false);}
        list.addDiffLog("备注",bfRole.getMemo(),rolePO.getMemo(),false);
        logService.addOperateLog("/admin/role/update",list);
        return rolePO;
    }
}
