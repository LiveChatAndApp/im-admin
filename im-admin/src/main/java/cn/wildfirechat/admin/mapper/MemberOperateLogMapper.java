package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.MemberOperateLogDTO;
import cn.wildfirechat.common.model.po.MemberOperateLogPO;
import cn.wildfirechat.common.model.query.MemberOperateLogQuery;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MemberOperateLogMapper {

	/**
	 * 新增操作日志
	 * 
	 * @param record
	 * @return
	 */
    int insert(MemberOperateLogPO record);

    /**
     * 查询操作日志
     * 
     * @return
     */
    List<MemberOperateLogDTO> find(MemberOperateLogQuery query);
    

}
