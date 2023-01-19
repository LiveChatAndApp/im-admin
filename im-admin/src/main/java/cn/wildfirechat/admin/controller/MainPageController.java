package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.*;
import cn.wildfirechat.common.model.form.*;
import cn.wildfirechat.common.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin/mainPage")
@Api(tags = "后台首页")
public class MainPageController extends BaseController {

    @Resource
    private MainPageService mainPageService;

    @GetMapping(value = "/info")
    @ApiOperation(value = "后台首页-资讯", httpMethod = "GET")
    public ResponseVO<?> mainPageInfo(MainPageForm form){
        log.info("mainPageInfo form: {}", form);
        MainPageInfoVO mainPageInfoVO = mainPageService.mainPageInfo(form.getDate());
        return ResponseVO.success(mainPageInfoVO);
    }

}
