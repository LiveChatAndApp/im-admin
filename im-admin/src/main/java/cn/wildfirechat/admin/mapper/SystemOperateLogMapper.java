package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.MemberOperateLogDTO;
import cn.wildfirechat.common.model.dto.SystemOperateLogDTO;
import cn.wildfirechat.common.model.po.MemberOperateLogPO;
import cn.wildfirechat.common.model.po.SystemOperateLogPO;
import cn.wildfirechat.common.model.query.MemberOperateLogQuery;
import cn.wildfirechat.common.model.query.SystemOperateLogQuery;

import java.util.List;

public interface SystemOperateLogMapper {

	/**
	 * 新增操作日志
	 * 
	 * @param record
	 * @return
	 */
    int insert(SystemOperateLogPO record);

    /**
     * 查询操作日志
     * 
     * @return
     */
    List<SystemOperateLogDTO> find(SystemOperateLogQuery query);
    

}
