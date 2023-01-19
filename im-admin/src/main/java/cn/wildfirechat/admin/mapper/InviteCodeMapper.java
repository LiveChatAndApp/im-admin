package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.query.InviteCodePageQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.vo.InviteCodePageVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InviteCodeMapper {

    List<InviteCodePageVO> selectInviteCodePageVO(InviteCodePageQuery query);

    List<InviteCodePO> list(InviteCodeQuery query);

    Long insert(InviteCodePO po);

    int update(InviteCodePO po);

    int updateFriendDefaultType(int defaultTypeCurrent, int defaultTypeTarget, String updater, Integer updaterRole);
}
