package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.common.support.ResultMsg;
import cn.wildfirechat.admin.mapper.AdminAuthMapper;
import cn.wildfirechat.admin.mapper.AdminUserMapper;
import cn.wildfirechat.admin.security.JwtTokenUtil;
import cn.wildfirechat.admin.security.SecurityUserMenu;
import cn.wildfirechat.common.model.dto.AdminUserAuthInfoDTO;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.vo.ResponseVO;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/session")
@Api(tags = "session管理")
public class SessionController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminAuthMapper adminAuthMapper;


    @GetMapping("/get")
    @ApiOperation(value = "取得session")
    public void getSession(@RequestParam("token") String token,
                                             HttpServletRequest servletRequest,
                                             HttpServletResponse servletResponse)  {
        log.info(" getSession {}", token);
        String username = null;
        JSONObject data = new JSONObject();
        try{
            username = jwtTokenUtil.getUserNameFromJwtToken(token);

            AdminUserAuthInfoDTO admin = adminUserMapper.getAuthInfoByUsername(username);
            List<SecurityUserMenu> adminMenus = getAdminMenus(admin);

            data.put("token", token);
            data.put("username", username);
            data.put("roleId", admin.getRoleId());
            data.put("roleLevel", admin.getRoleLevel());
            data.put("brand", admin.getBrandName());
            data.put("menus", adminMenus);
            servletResponse.setStatus(HttpStatus.OK.value());
            try {
                servletResponse.getWriter().write(objectMapper.writeValueAsString(ResponseVO.success(data)));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            log.info("getSession:: Unable to get JWT Token");
        }catch (ExpiredJwtException e) {
            log.info("getSession:: JWT Token has expired");
        }

        try {
            servletResponse.getWriter().write(JSONObject.toJSONString(ResultMsg.failure(401, "登录过期")));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 管理员菜单
     */
    private List<SecurityUserMenu> getAdminMenus(AdminUserAuthInfoDTO admin) {
        if (admin == null || 1 != admin.getStatus()) {
            return Collections.emptyList();
        }

        List<AdminAuthPO> adminAuthlist = adminAuthMapper.selectAuthByRoles(admin.getRoleId());

        if (CollectionUtils.isEmpty(adminAuthlist)) {
            return Collections.emptyList();
        }

        List<SecurityUserMenu> menus = new ArrayList<>();
        for (AdminAuthPO auth : adminAuthlist) {
            SecurityUserMenu userMenu = new SecurityUserMenu(auth.getId(), auth.getParentId(), auth.getName(), auth.getCode(), auth.getApi(), auth.getOrder());
            menus.add(userMenu);
        }


        return menus;
    }
}


