package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.config.RedisConfig;
import cn.wildfirechat.admin.mapper.SensitiveWordHitMapper;
import cn.wildfirechat.common.model.bo.SensitiveWordAddBO;
import cn.wildfirechat.common.model.po.SensitiveWordHitPO;
import cn.wildfirechat.common.utils.DateUtils;
import cn.wildfirechat.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
class MainPageServiceTest {

    @Autowired
    private RedisUtil redisUtil;

//    @Test
    public void insertRedisTest1() {
        Date tailDay = DateUtils.getTailDay(new Date());
        redisUtil.set("testEndTime", "1", 10);
    }

//    @Test
    public void insertRedisTest2() {
//        String count = (String)redisUtil.get(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY);
//        System.out.println("count"+count);
//        redisUtil.increment(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY, 1);
//        redisUtil.setExpireAt(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY, DateUtils.getTailDay(new Date()));
    }





}
