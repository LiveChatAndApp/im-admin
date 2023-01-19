package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.common.utils.UserAgentUtils;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.admin.service.AdminRoleService;
import cn.wildfirechat.admin.service.AdminUserService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.utils.ReflectionUtils;
import cn.wildfirechat.common.model.bo.AdminUserBO;
import cn.wildfirechat.common.model.enums.AdminRoleLevelEnum;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.po.AdminRolePO;
import cn.wildfirechat.common.model.po.AdminUserPO;
import cn.wildfirechat.common.model.query.AdminUserQuery;
import cn.wildfirechat.common.model.vo.*;
import cn.wildfirechat.common.support.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/user")
@Api(tags = "后台用户")
public class AdminUserController extends BaseController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminRoleService adminRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping(value = "/page")
    @ApiOperation(value = "后台用户-列表", httpMethod = "GET")
    public ResponseVO<?> page(AdminUserForm form, Page page) throws Exception {
        log.info("page form: {}", form);
        AdminUserQuery query = AdminUserQuery.builder().build();
        ReflectionUtils.copyFields(query, form, ReflectionUtils.STRING_TRIM_TO_NULL);

        if(AdminRoleLevelEnum.COMMON.getValue().equals(SpringSecurityUtil.getRoleLevel())){
            query.setLevel(AdminRoleLevelEnum.COMMON.getValue());
        }
        PageVO<AdminUserVO> pageVO = adminUserService.page(query, page);
        return ResponseVO.success(pageVO);
    }

    /**
     * 添加管理员
     */
    @PostMapping(value = "/insert")
    @ApiOperation(value = "账号-添加", httpMethod = "POST")
    public ResponseVO<?> createAdmin(HttpServletRequest request, @RequestBody @Valid AdminAddForm form) {
        try {
            log.info("insert form: {}", form);
            AdminUserBO bo = AdminUserBO.builder().build();
            ReflectionUtils.copyFields(bo, form, ReflectionUtils.STRING_TRIM_TO_NULL);
            bo.setChannel(UserAgentUtils.getOs(request));
//            String ip = IpTools.getIpAddr(request);

            adminUserService.add(bo);
            return ResponseVO.success(bo);
        } catch (IllegalArgumentException e) {
            log.error("createAdmin form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("createAdmin form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    /**
     * 编辑管理员
     */
//    @PreAuthorize("hasAuthority('CONSOLE_ADMIN_UPDATE')")
    @PostMapping(value = "/update")
    @ApiOperation(value = "账号-编辑", httpMethod = "POST")
    public ResponseVO<?> updateAdmin(@RequestBody @Valid AdminUpdateForm form) {
        try {
            log.info("update form: {}", form);
            AdminUserPO po = AdminUserPO.builder().build();
            ReflectionUtils.copyFields(po, form, ReflectionUtils.STRING_TRIM_TO_NULL);
//            String ip = IpTools.getIpAddr(request);

            adminUserService.update(po);
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("updateAdmin form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("updateAdmin form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    /**
     * 重置管理员密码
     */
//    @PreAuthorize("hasAuthority('CONSOLE_ADMIN_UPDATE')")
    @PostMapping(value = "/resetPwd")
    @ApiOperation(value = "账号-重置密码", httpMethod = "POST")
    public ResponseVO<?> resetPwd(@RequestBody @Valid AdminResetPwdForm form) {
        try {
            log.info("resetPwd form: {}", form);
            String newPwd = adminUserService.updatePassword(form.getId(), form.getPassword());
            return ResponseVO.success(newPwd);
        } catch (IllegalArgumentException e) {
            log.error("resetPwd form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("resetPwd form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    /**
     * 踢出管理员下线
     */
//    @PreAuthorize("hasAuthority('CONSOLE_ADMIN_UPDATE')")
    @PostMapping(value = "/kickOut")
    @ApiOperation(value = "账号-踢出下线", httpMethod = "POST")
    public ResponseVO<?> kickOut(@RequestBody @Valid AdminKickOutForm form) {
        try {
            log.info("kickOut form: {}", form);
            AdminUserPO admin = adminUserService.info(form.getId());
            redisUtil.delete(admin.getUsername());
            //后台账号-踢出下线log
            OperateLogList list = new OperateLogList();
            list.addLog("用户账号",admin.getUsername(),false);
            logService.addOperateLog("/admin/user/kickOut",list);
            return ResponseVO.success();
        } catch (IllegalArgumentException e) {
            log.error("kickOut form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("kickOut form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    /**
     * 账号资讯
     */
    @GetMapping(value = "/info")
    @ApiOperation(value = "账号-资讯", httpMethod = "GET")
    public ResponseVO<?> info() {
        try {
            Long adminId = SpringSecurityUtil.getUserId();
            AdminUserPO info = adminUserService.info(adminId);
            AdminUserInfoVO vo = new AdminUserInfoVO();
            ReflectionUtils.copyFields(vo, info, ReflectionUtils.STRING_TRIM_TO_NULL);
            AdminRolePO rolePo = adminRoleService.selectById(vo.getRoleId());
            vo.setRoleName(rolePo.getName());

            return ResponseVO.success(vo);
        } catch (IllegalArgumentException e) {
            log.error("adminInfo e: {}", e.getMessage(), e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("adminInfo e: {}", e.getMessage(), e);
            return ResponseVO.error(e.getMessage());
        }

    }


//    private void test() {
//        AdminHttpUtils. init("http://wildfirechat.cn:18080", "37923");
//        try {
//            IMResult<InputOutputUserInfo> userByMobile = UserAdmin.getUserByMobile("13888888888");
//            System.out.println(userByMobile.msg);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
