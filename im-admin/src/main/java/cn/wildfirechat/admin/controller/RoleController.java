package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.AdminRoleService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.form.RoleAddForm;
import cn.wildfirechat.common.model.form.RoleQueryForm;
import cn.wildfirechat.common.model.form.RoleUpdateForm;
import cn.wildfirechat.common.model.po.AdminRolePO;
import cn.wildfirechat.common.model.query.AdminRoleQuery;
import cn.wildfirechat.common.model.vo.AdminRoleVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/admin/role")
@Api(tags = "角色")
public class RoleController extends BaseController {

    @Autowired
    private AdminRoleService adminRoleService;


    @GetMapping(value = "/page")
    @ApiOperation(value = "角色-列表", httpMethod = "GET")
    public ResponseVO<?> page(RoleQueryForm form, Page page) {
        try {
            log.info("page form: {}, page: {}", form, page);
            AdminRoleQuery query = AdminRoleQuery.builder().build();
            ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            PageVO<AdminRoleVO> pageVO = adminRoleService.page(query, page);
            return ResponseVO.success(pageVO);
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }


    /**
     * 添加角色
     */
    @PostMapping(value = "/page")
    @ApiOperation(value = "角色-添加", httpMethod = "POST")
    public ResponseVO<?> addRole(@RequestBody @Valid RoleAddForm form) {
        try {
            log.info("addRole form: {}", form);

            AdminRolePO role = new AdminRolePO();
            ReflectionUtils.copyFields(role, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            role.setBrandName("SYSTEMADMIN");
            role.setCreateTime(new Date());

            AdminRolePO insertRole = adminRoleService.insert(role, form.getAdminAuthList());
            //角色-新增 log
            OperateLogList list = new OperateLogList();
            list.addLog("角色名称",form.getName(),false);
            list.addLog("用户层级",form.getLevel(),false);
            if (StringUtils.isNotBlank(form.getMemo())){
                list.addLog("备注",form.getMemo(),false);
            }
            logService.addOperateLog("/admin/role/add",list);
            return ResponseVO.success(insertRole);
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }

    }

    /**
     * 更新角色
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "角色-更新", httpMethod = "POST")
    public ResponseVO<?> updateRole(@RequestBody @Valid RoleUpdateForm form) {
        try {
            log.info("updateRole form: {}, page: {}", form);
            AdminRolePO role = new AdminRolePO();
            ReflectionUtils.copyFields(role, form, ReflectionUtils.STRING_TRIM_TO_NULL);

            AdminRolePO updatedRole = adminRoleService.update(role, form.getAdminAuthList());
            return ResponseVO.success(updatedRole);
        } catch (IllegalArgumentException e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("page form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }

    }

}
