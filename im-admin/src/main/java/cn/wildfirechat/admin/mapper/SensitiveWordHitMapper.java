package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.SensitiveWordHitDTO;
import cn.wildfirechat.common.model.po.SensitiveWordHitPO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface SensitiveWordHitMapper {

	/**
	 * 添加命中敏感词
	 * 
	 * @param record
	 * @return
	 */
    int insert(SensitiveWordHitPO recozrd);

	/**
	 * 批量删除命中敏感词
	 *
	 * @param ids
	 * @return
	 */
	int deleteBatch(@Param("ids") List<Long> ids);

	/**
	 * 删除所有命中敏感词
	 *
	 * @return
	 */
	int deleteAll();

    /**
     * 查询命中敏感词
     * 
     * @return
     */
    List<SensitiveWordHitDTO> find(@Param("memberName") String memberName);


	/**
	 * 查询命中敏感词
	 *
	 * @return
	 */
	List<SensitiveWordHitDTO> findConsiderAdminCondition();

	/**
	 * 查询命中敏感词 ids
	 *
     * @param list
	 * @return
	 */
	List<SensitiveWordHitDTO> selectByIds(@Param("list") Collection<Long> list);

    

}
