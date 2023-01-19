package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.mapper.MainPageInfoMapper;
import cn.wildfirechat.common.model.po.MainPageInfoPO;
import cn.wildfirechat.common.model.vo.ActiveGroupVO;
import cn.wildfirechat.common.model.vo.ActiveMemberVO;
import cn.wildfirechat.common.model.vo.MainPageInfoVO;
import cn.wildfirechat.common.utils.DateUtils;
import cn.wildfirechat.common.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class MainPageService extends BaseService {

	@Resource
	private MainPageInfoMapper mainPageInfoMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private ObjectMapper objectMapper;

	public MainPageInfoVO mainPageInfo(String date) {
		if(StringUtil.isNotBlank(date)){
			Assert.isTrue(DateUtils.dateStrIsValid(date, "yyyy-MM-dd"), "日期格式不符");
		}
		else{
			date = DateUtils.getNow("yyyy-MM-dd");
		}

		MainPageInfoPO mainPageInfoPO = mainPageInfoMapper.find(date);
		MainPageInfoVO result = MainPageInfoVO.builder().build();
		if(null != mainPageInfoPO){
			BeanUtils.copyProperties(mainPageInfoPO, result, "top10ActiveMember", "top10ActiveGroup");
//			ReflectionUtils.copyFields(mainPageInfoPO, result, ReflectionUtils.STRING_TRIM_TO_NULL);
			try{
				List<ActiveMemberVO> activeMemberVOList = objectMapper.readValue(mainPageInfoPO.getTop10ActiveMember(), new TypeReference<List<ActiveMemberVO>>() {});
				activeMemberVOList.sort(Comparator.comparing(ActiveMemberVO::getRange));
				result.setTop10ActiveMember(activeMemberVOList);

				List<ActiveGroupVO> activeGroupVOList = objectMapper.readValue(mainPageInfoPO.getTop10ActiveGroup(), new TypeReference<List<ActiveGroupVO>>() {});
				activeGroupVOList.sort(Comparator.comparing(ActiveGroupVO::getRange));
				result.setTop10ActiveGroup(activeGroupVOList);
			}catch (Exception e){
				log.info("查询首页资讯错误 message:{}", e.getMessage());
			}
		}

		return result;
	}


}
