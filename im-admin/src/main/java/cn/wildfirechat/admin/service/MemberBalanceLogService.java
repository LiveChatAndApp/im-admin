package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.MemberBalanceLogMapper;
import cn.wildfirechat.common.model.dto.MemberBalanceLogDTO;
import cn.wildfirechat.common.model.query.MemberBalanceLogQuery;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import cn.wildfirechat.common.support.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class MemberBalanceLogService {
    @Resource
    private MemberBalanceLogMapper memberBalanceLogMapper;

    @Transactional(readOnly = true)
    public PageVO<MemberBalanceLogDTO> page(MemberBalanceLogQuery query, Page page) {
        page.startPageHelper();
        List<MemberBalanceLogDTO> dtos = memberBalanceLogMapper.selectFundDetails(query);
        return new PageInfo<>(dtos).convertToPageVO();
    }
}
