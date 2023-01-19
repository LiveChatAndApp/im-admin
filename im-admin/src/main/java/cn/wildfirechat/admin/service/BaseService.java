package cn.wildfirechat.admin.service;

import cn.wildfirechat.common.support.SpringMessage;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

@Slf4j
public class BaseService {

    @Resource
    protected SpringMessage message;

    @Resource
    protected LogService logService;
}
