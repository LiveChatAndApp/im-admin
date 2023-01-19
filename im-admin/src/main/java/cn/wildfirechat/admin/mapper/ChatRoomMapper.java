package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.ChatRoomPO;
import cn.wildfirechat.common.model.query.ChatRoomQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ChatRoomMapper {
    int insert(ChatRoomPO po);
    int update(ChatRoomPO po);
    List<ChatRoomPO> list(ChatRoomQuery query);
    ChatRoomPO selectById(@Param("id") Long id);
    ChatRoomPO selectByCid(@Param("cid") String cid);
    List<ChatRoomPO> selectChatRoomByIds(@Param("list") Collection<Long> list);
}
