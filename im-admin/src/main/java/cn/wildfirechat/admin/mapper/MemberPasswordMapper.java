package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.SystemOperateLogDTO;
import cn.wildfirechat.common.model.po.MemberPasswordPO;
import cn.wildfirechat.common.model.po.SystemOperateLogPO;
import cn.wildfirechat.common.model.query.MemberPasswordQuery;
import cn.wildfirechat.common.model.query.SystemOperateLogQuery;

import java.util.List;

public interface MemberPasswordMapper {

	/**
	 * 新增用户密码
	 *
	 * @param record
	 * @return
	 */
	int insert(MemberPasswordPO record);

	/**
	 * 更新用户密码
	 *
	 * @param record
	 * @return
	 */
	int updatePassword(MemberPasswordPO record);


	/**
	 * 更新用户密码
	 *
	 * @param record
	 * @return
	 */
	int update(MemberPasswordPO record);

    /**
     * 查询用户密码
     * 
     * @return
     */
    MemberPasswordPO find(MemberPasswordQuery query);
    

}
