package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.SensitiveWordPO;
import cn.wildfirechat.common.model.query.SensitiveWordQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensitiveWordMapper {

	/**
	 * 新增敏感词
	 * 
	 * @param record
	 * @return
	 */
    int insert(SensitiveWordPO record);


	/**
	 * 新增敏感词
	 *
	 * @param list
	 * @return
	 */
	int insertBatch(@Param("list") List<SensitiveWordPO> list);


	/**
	 * 批量删除命中敏感词
	 *
	 * @param ids
	 * @return
	 */
	int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 查询敏感词
     * 
     * @return
     */
    List<SensitiveWordPO> find(SensitiveWordQuery query);

    /**
     * 查询敏感词存在
     *
     * @return
     */
    List<SensitiveWordPO> isExist(List<String> contentList);

    /**
     * 查询敏感词存在
     *
     * @return
     */
    List<SensitiveWordPO> isExistById(@Param(value = "idList") List<Long> idList);



}
