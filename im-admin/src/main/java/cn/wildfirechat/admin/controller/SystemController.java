package cn.wildfirechat.admin.controller;

import cn.wildfirechat.common.ErrorCode;
import cn.wildfirechat.common.model.dto.SystemVersionDto;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.pojos.OutputCreateChatroom;
import cn.wildfirechat.sdk.model.IMResult;
import cn.wildfirechat.sdk.utilities.AdminHttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags = "版本管理")
public class SystemController {

    public static final String VERSION = "/version";

    @Value("${system.admin.version}")
    private String version;

    @Value("${wildfirechat.app.host}")
    private String appHost;

    @Value("${wildfirechat.im.host}")
    private String imHost;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = VERSION)
    @ApiOperation(value = "模组版本列表")
    public ResponseVO<?> getVersion() {
        List<SystemVersionDto> dtos = new ArrayList<SystemVersionDto>();
        SystemVersionDto dto = SystemVersionDto.builder()
                .projectCode("ADMIN")
                .projectVersion(version)
                .build();
        dtos.add(dto);

        SystemVersionDto result;
        try {
            result = restTemplate.getForObject(appHost+"/app/version", SystemVersionDto.class);
            dtos.add(result);
        } catch (Exception e) {
            log.error("getVersion APP ERROR: {}", e.getMessage());
            dtos.add(SystemVersionDto.builder().projectCode("APP").projectVersion("ERROR").build());
        }

        try {
            IMResult<SystemVersionDto> imResult = AdminHttpUtils.httpJsonPost("/admin/im/version", null, SystemVersionDto.class);
            if (imResult.getErrorCode() == ErrorCode.ERROR_CODE_SUCCESS) {
                dtos.add(imResult.getResult());
            }else{
                log.error("getVersion IM ERROR, code :{} , message :{}", imResult.getCode(), imResult.getMsg());
                dtos.add(SystemVersionDto.builder().projectCode("IM").projectVersion("ERROR").build());
            }
        } catch (Exception e) {
            log.error("getVersion IM ERROR: {}", e.getMessage());
            dtos.add(SystemVersionDto.builder().projectCode("IM").projectVersion("ERROR").build());
        }

        return ResponseVO.success(dtos);
    }
}
