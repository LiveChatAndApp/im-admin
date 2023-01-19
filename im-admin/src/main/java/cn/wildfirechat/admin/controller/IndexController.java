package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.AdminRoleService;
import cn.wildfirechat.common.model.query.AdminRoleQuery;
import cn.wildfirechat.common.model.vo.AdminRoleVO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/indexing")
@Api(tags = "索引下选单")
public class IndexController {

    @Autowired
    private AdminRoleService adminRoleService;


    @GetMapping(value = "/role")
    @ApiOperation(value = "新增账号_角色", httpMethod = "GET")
    public ResponseVO<?> getRoleList() {
        try {
            log.info("查询索引_新增账号下拉角色选单");
            AdminRoleQuery query = AdminRoleQuery.builder().build();
            List<AdminRoleVO> list = adminRoleService.list(query);
            return ResponseVO.success(list);
        } catch (IllegalArgumentException e) {
            log.error("getRoleList error: {}", e.getMessage(), e);
            return ResponseVO.error(e.getMessage());
        } catch (Exception e) {
            log.error("getRoleList error: {}", e.getMessage(), e);
            return ResponseVO.error(e.getMessage());
        }
    }




}
