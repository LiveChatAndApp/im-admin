package cn.wildfirechat.admin.controller;

import cn.wildfirechat.common.model.dto.HazelCastOpResultDto;
import cn.wildfirechat.common.model.dto.HazelCastOperation;
import cn.wildfirechat.common.model.dto.SystemVersionDto;
import cn.wildfirechat.sdk.UserAdmin;
import cn.wildfirechat.sdk.model.IMResult;
import org.springframework.web.bind.annotation.*;

import cn.wildfirechat.common.model.form.ConnectDataForm;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.sdk.utilities.AdminHttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin/system/test")
@Api(tags = "測試類")
public class TestController {
	@Resource
	private RestTemplate restTemplate;

	@GetMapping(value = "/connection")
	@ApiOperation(value = "連線")
	public ResponseVO<?> connect(ConnectDataForm form) {
		try {
			IMResult<Void> imResult = AdminHttpUtils.httpJsonPost("/admin/connect/add", form, Void.class);
			log.info("連線: {}", imResult);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ResponseVO.success();
	}

	@PostMapping(value = "/hazelcast")
	@ApiOperation(value = "HazelCast_測試", httpMethod = "POST")
	public ResponseVO<?> doHazelcast(@RequestBody HazelCastOperation operation) {
		try {
			log.info("HazelCast_測試:  operation:{}", operation);

			String path = "/admin/hazelcast/map/operation";
			IMResult<HazelCastOpResultDto> imResult = AdminHttpUtils.httpJsonPost(path, operation, HazelCastOpResultDto.class);
			HazelCastOpResultDto dto = imResult.getResult();
			return ResponseVO.success(dto);
		} catch (IllegalArgumentException e) {
			log.error("doHazelcast error: {}", e.getMessage(), e);
			return ResponseVO.error(e.getMessage());
		} catch (Exception e) {
			log.error("doHazelcast error: {}", e.getMessage(), e);
			return ResponseVO.error(e.getMessage());
		}
	}
}
