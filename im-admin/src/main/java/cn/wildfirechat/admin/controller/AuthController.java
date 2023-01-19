package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.AdminAuthService;
import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.common.model.form.AuthQueryForm;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import cn.wildfirechat.common.model.vo.AdminAuthTreeVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.model.vo.TreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/auth")
@Api(tags = "菜单")
public class AuthController {

    @Autowired
    private AdminAuthService adminAuthService;

    @ApiOperation(value = "菜单权限-列表", httpMethod = "GET")
    @GetMapping(value = "/list")
    public ResponseVO<?> list(HttpServletResponse resp, @Valid AuthQueryForm form) {
        try {
            log.info("list form: {}", form);
            Long roleId = form.getRoleId();
            List<AdminAuthPO> authList = roleId == null
                    ? adminAuthService.list(new AdminAuthQuery())
                    : adminAuthService.getByRoleId(roleId);

            List<AdminAuthTreeVO> AdminAuthTreeVOs = buildAclTree(authList);
            return ResponseVO.success(AdminAuthTreeVOs);
        } catch (IllegalArgumentException e) {
            log.error("list form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("list form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }
    }

    /**
     * 查询需要记录日志的功能
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "系统日志-选单", httpMethod = "GET")
    @GetMapping(value = "/opLogAuth/list")
    public ResponseVO<?> getOpLogAuthList(@Valid AuthQueryForm form) {
        try {
            log.info("operateLog/list form: {}", form);
            Long roleId = form.getRoleId();
            List<AdminAuthPO> authList = roleId == null
                    ? adminAuthService.list(new AdminAuthQuery())
                    : adminAuthService.getByRoleId(roleId);

            List<AdminAuthTreeVO> buildAuthTree = buildAclTree(authList);

            List<AdminAuthTreeVO> extraAclList = new ArrayList<AdminAuthTreeVO>();
            for(Map.Entry<Long, Pair<String,String>> entry : LogService.getExtraApiMap().entrySet()) {
                AdminAuthTreeVO vo = new AdminAuthTreeVO();
                vo.setId(entry.getKey());
                vo.setParentId(-1L);
                vo.setChildren(new ArrayList<TreeNode>());
                vo.setApi(entry.getValue().getFirst().toUpperCase());
                vo.setName(entry.getValue().getSecond());
                vo.setApi(entry.getValue().getFirst());
                vo.setIsLog(true);
                extraAclList.add(vo);
            }
            extraAclList.addAll(buildAuthTree);
            extraAclList.forEach(acl -> {
                acl.setIsLog(true);
            });
            return ResponseVO.success(extraAclList);
        } catch (IllegalArgumentException e) {
            log.error("list form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("list form: {}", form, e);
            return ResponseVO.error(e.getMessage());
        }

    }

    private List<AdminAuthTreeVO> buildAclTree(List<AdminAuthPO> authList) {
        return AdminAuthTreeVO.buildTree(
                authList.stream()
                        .map(adminAuth -> {
                            AdminAuthTreeVO dto = new AdminAuthTreeVO();
                            dto.setId(adminAuth.getId());
                            dto.setParentId(adminAuth.getParentId());
                            dto.setCode(adminAuth.getCode());
                            dto.setName(adminAuth.getName().replaceAll("#", "-"));
                            dto.setApi(adminAuth.getApi());
                            dto.setIsLog(adminAuth.getIsLog());
                            return dto;
                        })
                        .collect(Collectors.toList()));
    }
}
