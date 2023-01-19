package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.MessagePO;
import cn.wildfirechat.common.model.query.MessagePageQuery;
import cn.wildfirechat.common.model.query.MessageQuery;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MessageMapper {

    List<MessagePO> list(MessageQuery query);

    List<MessagePO> page(MessagePageQuery query);

    List<MessagePO> selectDeleted();

    int insert(MessagePO po);

    int insertUpdate(MessagePO po);

    int update(MessagePO po);

    int revert(Long memberId, Long memberId2, String memo, Integer count);

    int delete(Long memberId, Long memberId2, String memo, Integer count);

    Long countCreateAtCurrDate();

    List<MessagePO> lastChats(MessageQuery query);
}
