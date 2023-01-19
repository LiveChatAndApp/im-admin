package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.AdminAuthMapper;
import cn.wildfirechat.admin.mapper.AdminUserMapper;
import cn.wildfirechat.admin.mapper.MemberOperateLogMapper;
import cn.wildfirechat.admin.utils.UploadFileUtils;
import cn.wildfirechat.common.model.dto.MemberOperateLogDTO;
import cn.wildfirechat.common.model.dto.OperateLogMemoDto;
import cn.wildfirechat.common.model.enums.AppOperateLogEnum;
import cn.wildfirechat.common.model.po.AdminAuthPO;
import cn.wildfirechat.common.model.query.AdminAuthQuery;
import cn.wildfirechat.common.model.query.MemberOperateLogQuery;
import cn.wildfirechat.common.model.vo.MemberOperateLogVO;
import cn.wildfirechat.common.model.vo.MemberVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import cn.wildfirechat.common.utils.FormDataUtil;
import cn.wildfirechat.common.utils.StringUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberOperateLogService {

    @Resource
    private MemberOperateLogMapper memberOperateLogMapper;

    @Resource
    private AdminAuthMapper adminAuthMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UploadFileUtils uploadFileUtils;

    @Transactional(readOnly = true)
    public PageVO<MemberOperateLogVO> page(MemberOperateLogQuery query, Page page) {
        page.startPageHelper();
        List<MemberOperateLogDTO> list = memberOperateLogMapper.find(query);
        PageVO<MemberOperateLogDTO> memberOperateLogDTOPageVO = new PageInfo<>(list).convertToPageVO();

        // 查权限表(key=权限编号，val=权限名称)
        Map<Long, String> authMap = Arrays.stream(AppOperateLogEnum.values())
//                .peek(System.out::println)
                .collect(Collectors.toMap(AppOperateLogEnum::getKey, AppOperateLogEnum::getName));

        List<MemberOperateLogVO> vos = new ArrayList<>();
        for(MemberOperateLogDTO dto : list) {
            MemberOperateLogVO vo = MemberOperateLogVO.builder().build();
            try {
//                ReflectionUtils.copyFields(vo, dto, ReflectionUtils.STRING_TRIM_TO_NULL);
                BeanUtils.copyProperties(dto, vo, "memo", "authName");
                // 设置功能名称
                vo.setAuthName(authMap.get(dto.getAuthId()));
                vo.setMemo(objectMapper.readValue(dto.getMemo(), OperateLogMemoDto.class));
                vo.setAvatarUrl(StringUtil.isNotBlank(vo.getAvatarUrl())?uploadFileUtils.parseFilePathToUrl(vo.getAvatarUrl()):vo.getAvatarUrl());
                vos.add(vo);
            }catch (Exception e) {
                log.info("查询操作日志错误, exception:{}", e.getMessage());
            }
        }

        PageVO<MemberOperateLogVO> pageVO = new PageInfo<>(vos).convertToPageVO();
        pageVO.setTotal(memberOperateLogDTOPageVO.getTotal());
        pageVO.setTotalPage(memberOperateLogDTOPageVO.getTotalPage());
        return pageVO;
    }

    @Transactional(readOnly = true)
    public List<MemberOperateLogVO> list(MemberOperateLogQuery query) {
        List<MemberOperateLogDTO> list = memberOperateLogMapper.find(query);

        // 查权限表(key=权限编号，val=权限名称)
        Map<Long, String> authMap = Arrays.stream(AppOperateLogEnum.values())
//                .peek(System.out::println)
                .collect(Collectors.toMap(AppOperateLogEnum::getKey, AppOperateLogEnum::getName));

        List<MemberOperateLogVO> vos = new ArrayList<>();
        for(MemberOperateLogDTO dto : list) {
            MemberOperateLogVO vo = MemberOperateLogVO.builder().build();
            try {
//                ReflectionUtils.copyFields(vo, dto, ReflectionUtils.STRING_TRIM_TO_NULL);
                BeanUtils.copyProperties(dto, vo, "memo", "authName");
                // 设置功能名称
                vo.setAuthName(authMap.get(dto.getAuthId()));
                vo.setMemo(objectMapper.readValue(dto.getMemo(), OperateLogMemoDto.class));
                vos.add(vo);
            }catch (Exception e) {
                log.info("查询操作日志列表 错误, exception:{}", e.getMessage());
            }
        }

        return vos;
    }


}
