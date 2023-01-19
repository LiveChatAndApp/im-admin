package cn.wildfirechat.admin.config;

import cn.wildfirechat.admin.common.utils.RateLimiter;
import cn.wildfirechat.sdk.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class AdminHttpUtilsConfig {


    @Autowired
    private IMConfig mIMConfig;

    private RateLimiter rateLimiter;


    @PostConstruct
    private void init() {
        AdminConfig.initAdmin(mIMConfig.admin_url, mIMConfig.admin_secret);
        rateLimiter = new RateLimiter(60, 200);
    }
}
