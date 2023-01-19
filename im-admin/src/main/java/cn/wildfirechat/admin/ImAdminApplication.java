package cn.wildfirechat.admin;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("cn.wildfirechat.admin.mapper")
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "${nacos.config.server-addr}"))//18.166.154.97:8848
@NacosPropertySources({
		@NacosPropertySource(dataId = "im.admin.config", groupId="im.admin.group", autoRefreshed = true, type = ConfigType.PROPERTIES)
})
//@ComponentScan(basePackages = {"cn.wildfirechat.common"})
public class ImAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(ImAdminApplication.class, args);
	}
}
