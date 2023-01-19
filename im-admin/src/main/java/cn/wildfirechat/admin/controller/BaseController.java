package cn.wildfirechat.admin.controller;

import cn.wildfirechat.admin.service.LogService;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.support.SpringMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Slf4j
public class BaseController {

    @Resource
    protected SpringMessage message;

    @Resource
    protected LogService logService;
}
