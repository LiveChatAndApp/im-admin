package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.OtpLogMapper;
import cn.wildfirechat.common.model.po.OtpLogPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OtpLogService {

    @Autowired
    private OtpLogMapper otpLogMapper;



//    @Transactional(rollbackFor = Exception.class)
    public int insert(OtpLogPO optLogPO) {
        log.info("OtpLogService:::insert:{}", optLogPO);
        return otpLogMapper.insert(optLogPO);
    }






}
