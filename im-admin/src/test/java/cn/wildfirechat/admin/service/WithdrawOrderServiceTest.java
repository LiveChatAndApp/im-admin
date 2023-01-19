package cn.wildfirechat.admin.service;

import cn.wildfirechat.common.model.dto.OperateTotalDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cn.wildfirechat.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
//@SpringBootTest
class WithdrawOrderServiceTest {

	@Autowired
	private WithdrawOrderService withdrawOrderService;

//	@Test
	public void pageTest() {
		List<OperateTotalDTO> operateTotalDTOList = withdrawOrderService.totalByDate(DateUtils.parse("2022-11-01", "yyyy-MM-dd"));
		log.info("输出:{}", operateTotalDTOList);
	}

}
