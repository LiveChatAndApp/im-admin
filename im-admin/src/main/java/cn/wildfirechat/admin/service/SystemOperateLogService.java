package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.AdminAuthMapper;
import cn.wildfirechat.admin.mapper.AdminUserMapper;
import cn.wildfirechat.admin.mapper.MemberOperateLogMapper;
import cn.wildfirechat.admin.mapper.SystemOperateLogMapper;
import cn.wildfirechat.common.model.dto.MemberOperateLogDTO;
import cn.wildfirechat.common.model.dto.OperateLogMemoDto;
import cn.wildfirechat.common.model.dto.SystemOperateLogDTO;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import cn.wildfirechat.common.model.query.MemberOperateLogQuery;
import cn.wildfirechat.common.model.query.SystemOperateLogQuery;
import cn.wildfirechat.common.model.vo.MemberOperateLogVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.model.vo.SystemOperateLogVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.FormDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemOperateLogService {

    @Resource
    private SystemOperateLogMapper systemOperateLogMapper;

    @Resource
    private AdminAuthMapper adminAuthMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private LogService logService;


    @Transactional(readOnly = true)
    public PageVO<SystemOperateLogVO> page(SystemOperateLogQuery query, Page page) {
        page.startPageHelper();
        List<SystemOperateLogDTO> list = systemOperateLogMapper.find(query);
        PageVO<SystemOperateLogDTO> systemOperateLogDTOPageVO = new PageInfo<>(list).convertToPageVO();

        // 查权限表(key=权限编号，val=权限名称)
//        List<AdminAuthPO> authList = adminAuthMapper.list(AdminAuthQuery.builder().build());
//        Map<Long, String> authMap = authList.stream()
////                .peek(System.out::println)
//                .collect(Collectors.toMap(AdminAuthPO::getId, AdminAuthPO::getName));
//        Map<Long, Pair<String, String>> extraApiMap = LogService.getExtraApiMap();
//        for(Map.Entry<Long, Pair<String, String>> entry : extraApiMap.entrySet()) {
//            authMap.put(entry.getKey(), entry.getValue().getSecond());
//        }
        Map<Long, String> authMap = logService.getAuthMap();

        List<SystemOperateLogVO> vos = new ArrayList<>();
        for(SystemOperateLogDTO dto : list) {
            SystemOperateLogVO vo = SystemOperateLogVO.builder().build();
            try {
//                ReflectionUtils.copyFields(vo, dto, ReflectionUtils.STRING_TRIM_TO_NULL);
                BeanUtils.copyProperties(dto, vo, "memo", "authName");
                // 设置功能名称
                vo.setAuthName(authMap.get(dto.getAuthId()).replaceAll("#", "-"));
                vo.setMemo(objectMapper.readValue(dto.getMemo(), OperateLogMemoDto.class));
                vos.add(vo);
            }catch (Exception e) {
                log.info("查询系统操作日志错误, exception:{}", e.getMessage());
            }
        }

        PageVO<SystemOperateLogVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(systemOperateLogDTOPageVO.getTotal());
        pageVO.setTotalPage(systemOperateLogDTOPageVO.getTotalPage());
        return pageVO;
    }

    @Transactional(readOnly = true)
    public List<SystemOperateLogVO> list(SystemOperateLogQuery query) {
        List<SystemOperateLogDTO> list = systemOperateLogMapper.find(query);


//        // 查权限表(key=权限编号，val=权限名称)
//        List<AdminAuthPO> authList = adminAuthMapper.list(AdminAuthQuery.builder().build());
//        Map<Long, String> authMap = authList.stream()
////                .peek(System.out::println)
//                .collect(Collectors.toMap(AdminAuthPO::getId, AdminAuthPO::getName));
//        Map<Long, Pair<String, String>> extraApiMap = LogService.getExtraApiMap();
//        for(Map.Entry<Long, Pair<String, String>> entry : extraApiMap.entrySet()) {
//            authMap.put(entry.getKey(), entry.getValue().getSecond());
//        }
        Map<Long, String> authMap = logService.getAuthMap();


        List<SystemOperateLogVO> vos = new ArrayList<>();
        for(SystemOperateLogDTO dto : list) {
            SystemOperateLogVO vo = SystemOperateLogVO.builder().build();
            try {
//                ReflectionUtils.copyFields(vo, dto, ReflectionUtils.STRING_TRIM_TO_NULL);
                BeanUtils.copyProperties(dto, vo, "memo", "authName");
                // 设置功能名称
                vo.setAuthName(authMap.get(dto.getId()));
                vo.setMemo(objectMapper.readValue(dto.getMemo(), OperateLogMemoDto.class));
                vos.add(vo);
            }catch (Exception e) {
                log.info("查询系统操作日志错误, exception:{}", e.getMessage());
            }
        }

        return vos;
    }


}
