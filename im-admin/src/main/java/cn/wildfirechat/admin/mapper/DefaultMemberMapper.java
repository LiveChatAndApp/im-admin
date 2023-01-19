package cn.wildfirechat.admin.mapper;

import cn.wildfirechat.common.model.dto.DefaultMemberDTO;
import cn.wildfirechat.common.model.po.DefaultMemberPO;
import cn.wildfirechat.common.model.po.InviteCodePO;
import cn.wildfirechat.common.model.query.DefaultMemberQuery;
import cn.wildfirechat.common.model.query.InviteCodePageQuery;
import cn.wildfirechat.common.model.query.InviteCodeQuery;
import cn.wildfirechat.common.model.vo.DefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.InviteCodePageVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DefaultMemberMapper {

    Long insert(DefaultMemberPO po);

    List<DefaultMemberPageVO> selectDefaultMemberPageVO(DefaultMemberQuery defaultMemberQuery);

    List<DefaultMemberPO> list(DefaultMemberQuery defaultMemberQuery);

    Integer update(DefaultMemberPO defaultMemberPO);

    Integer delete(Long id);

    DefaultMemberDTO selectById(Long id);
}
