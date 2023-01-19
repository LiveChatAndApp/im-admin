package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.AdminAuthMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.admin.mapper.MemberOperateLogMapper;
import cn.wildfirechat.admin.mapper.SystemOperateLogMapper;
import cn.wildfirechat.admin.security.SpringSecurityUtil;
import cn.wildfirechat.common.model.dto.LogPairDto;
import cn.wildfirechat.common.model.dto.OperateLogList;
import cn.wildfirechat.common.model.dto.OperateLogMemoDto;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.po.MemberOperateLogPO;
import cn.wildfirechat.common.model.po.SystemOperateLogPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import cn.wildfirechat.common.utils.FormDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LogService {
	
	private static Map<Long, Pair<String,String>> extraApiMap = new HashMap<Long, Pair<String,String>>();
	static {
		// 注意key不能与t_admin_auth.AclId重复
		extraApiMap.put(-100L, Pair.of("/login", "登录"));//后台
		extraApiMap.put(-101L, Pair.of("/logout", "登出"));
	}
	
	@Resource
	private SystemOperateLogMapper systemOperateLogMapper;
	
	@Resource
	private AdminAuthMapper adminAuthMapper;
	
	@Resource
	private ObjectMapper objectMapper;
	
	@Resource
	private MemberMapper memberMapper;

	private Map<Long, String> authMap;// 查权限表(key=权限编号，val=权限名称)

	@PostConstruct
	public void init(){
		List<AdminAuthPO> authList = adminAuthMapper.list(AdminAuthQuery.builder().build());
		authMap = authList.stream()
//                .peek(System.out::println)
				.collect(Collectors.toMap(AdminAuthPO::getId, AdminAuthPO::getName));
		Map<Long, Pair<String, String>> extraApiMap = LogService.getExtraApiMap();
		for(Map.Entry<Long, Pair<String, String>> entry : extraApiMap.entrySet()) {
			authMap.put(entry.getKey(), entry.getValue().getSecond());
		}
	}

	public Map<Long, String> getAuthMap(){
		return authMap;
	}
	
	/**
	 * 新增操作日志
	 * 
	 * @param api			t_admin_auth.Api栏位值(例：/merchant/add)
	 * @param list			log物件
	 */
	public void addOperateLog(String api, OperateLogList list) {
		addOperateLog(api, list, null);
	}
	
	/**
	 * 新增操作日志
	 * 
	 * @param api			t_acl.Api栏位值(例：/merchant/add)
	 * @param list			log物件
	 */
	public void addOperateLog(String api, OperateLogList list, String merchantId) {
		try {
			AdminAuthPO auth = getAuth(api);
			if(auth == null || !auth.getIsLog()) {
				log.info("l表查无对应api=[{}]或该功能不支援记录log", api);
				return;
			}
			// before
			if(list == null || list.isEmpty()) {
				return;
			}

			SystemOperateLogPO record = SystemOperateLogPO.builder().build();
			record.setAdminId(SpringSecurityUtil.getUserId().toString());
			record.setAuthId(auth.getId());
			record.setMemo(objectMapper.writeValueAsString(OperateLogMemoDto.builder().before(list).after(list.getAfter()).build()));
			record.setCreator(SpringSecurityUtil.getPrincipal());
			record.setCreatorIp(SpringSecurityUtil.getSource());
			record.setCreatorLocation(getLocationByIP(SpringSecurityUtil.getSource()));
			record.setCreatorLevel(SpringSecurityUtil.getRoleLevel());
			record.setCreateTime(new Date());
			log.info("输出 systemOperateLog: {}", record);
			int result = systemOperateLogMapper.insert(record);
			log.info("add operate log result:{}", result);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("新增操作日志错误api=[{}], exception:{}", api, e.getMessage());
		}
	}

	public void addOperateLogForUpdate(String api, OperateLogList list) {
		addOperateLogForUpdate(api, list, null);
	}
	
	/**
	 * 新增操作日志(for Update)
	 * 
	 * @param api			t_acl.Api栏位值(例：/merchant/add)
	 * @param list			log物件
	 * @param merchantId	被修改的商户号
	 */
	public void addOperateLogForUpdate(String api, OperateLogList list, String merchantId) {
		try {

			AdminAuthPO auth = getAuth(api);
			if(auth == null || !auth.getIsLog()) {
				log.info("表查无对应api=[{}]或该功能不支援记录log", api);
				return;
			}
			if(list.getAfter().isEmpty()) {
				return;
			}
			
			// 移除before重复key
			LogPairDto copy = null;
			List<LogPairDto> temp = new ArrayList<LogPairDto>(list);
			for(LogPairDto pair : temp) {
				if(copy != null && pair.equals(copy) ) {
					list.remove(pair);
				}
				copy = pair;
			}
			
			String adminId = SpringSecurityUtil.getUserId().toString();
			String userName = SpringSecurityUtil.getPrincipal();
			SystemOperateLogPO record = SystemOperateLogPO.builder().build();
			record.setAdminId(adminId);
			record.setAuthId(auth.getId());
			record.setMemo(objectMapper.writeValueAsString(OperateLogMemoDto.builder().before(list).after(list.getAfter()).build()));
			record.setCreator(userName);
			record.setCreatorIp(SpringSecurityUtil.getSource());
			record.setCreatorLocation(getLocationByIP(SpringSecurityUtil.getSource()));
			record.setCreatorLevel(SpringSecurityUtil.getRoleLevel());
			record.setCreateTime(new Date());
			int result = systemOperateLogMapper.insert(record);
			log.info("add operate log result:{}", result);
		}catch (Exception e) {
			log.error("新增日志(for update)错误api=[{}], exception:{}", api, e.getMessage());
		}
	}
	



	/**
	 * 新增操作日志
	 *
	 * @param api			t_acl.Api栏位值(例：/merchant/add)
	 * @param list			log物件
	 */
	public void addLogInOutOperateLog(String api, OperateLogList list, String ip, String adminId, String userName, Integer roleLevel) {
		try {
			AdminAuthPO auth = getAuth(api);
			if(auth == null || !auth.getIsLog()) {
				log.info("l表查无对应api=[{}]或该功能不支援记录log", api);
				return;
			}
			// before
			if(list == null || list.isEmpty()) {
				return;
			}

			SystemOperateLogPO record = SystemOperateLogPO.builder().build();
			record.setAdminId(adminId == null ? SpringSecurityUtil.getUserId().toString() : adminId);
			record.setAuthId(auth.getId());
			record.setMemo(objectMapper.writeValueAsString(OperateLogMemoDto.builder().before(list).after(list.getAfter()).build()));
			record.setCreator(userName == null? SpringSecurityUtil.getPrincipal(): userName);
			record.setCreatorIp(ip);
			record.setCreatorLocation(getLocationByIP(ip));
			record.setCreatorLevel(roleLevel == null? SpringSecurityUtil.getRoleLevel(): roleLevel);
			record.setCreateTime(new Date());
			int result = systemOperateLogMapper.insert(record);
			log.info("add operate log result:{}", result);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("新增操作日志错误api=[{}], exception:{}", api, e.getMessage());
		}
	}

	/**
	 * 取得不在t_acl内的但需要增加log的接口
	 * 
	 * Pair.getFirst() = api
	 * Pair.getSecond() = name
	 * 
	 * @return
	 */
	public static Map<Long, Pair<String,String>> getExtraApiMap() {
		return extraApiMap;
	}
	
	/**
	 * 获取Acl
	 * 
	 * @param api
	 * @return
	 */
	private AdminAuthPO getAuth(String api) {
		AdminAuthPO auth = null;
		List<AdminAuthPO> aclList = adminAuthMapper.list(AdminAuthQuery.builder().api(api).build());
		if(!aclList.isEmpty()) {
			auth = aclList.get(0);
		}else {
			for(Entry<Long, Pair<String,String>> entry : extraApiMap.entrySet()) {
				if(entry.getValue().getFirst().equals(api)) {
					auth = AdminAuthPO.builder().id(entry.getKey()).isLog(true).build();
					break;
				}
			}
		}
		return auth;
	}

	/**
	 * 根据IP取得地理位置
	 *
	 * @return String
	 */
	private String getLocationByIP(String ip){
		StringBuilder sb = new StringBuilder().append("https://www.ip.cn/ip/").append(ip).append(".html");
		String requestUrl = sb.toString();
		log.info("搜寻ip:{} 地理位置, url:{}", ip, requestUrl);

		if(StringUtils.isBlank(ip)){
			return "";
		}
		if(ip.startsWith("192.168.") || ip.startsWith("10.")){
			return "内网IP";
		}

		Map<String, String> headerMap = new LinkedHashMap<>();
		Object request = new Object();
		String location = null;
		try {
			HttpResponse response = FormDataUtil.get(requestUrl, request, headerMap);
			String body = FormDataUtil.getBody(response);
			Document doc = Jsoup.parse(body);
			Element tab0_address = doc.getElementById("tab0_address");//解析tag为<div id="tab0_address">中国 移动</div>

			location = tab0_address.text();
			log.info("解析地理位置为:{}", location);
		} catch (Exception e) {
//            e.printStackTrace();
			log.info("搜寻解析ip地理位置出错, url:{}", requestUrl);
			location = "";
		}

		return location;

	}

}

