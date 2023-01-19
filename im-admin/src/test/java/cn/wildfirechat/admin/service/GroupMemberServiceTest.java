package cn.wildfirechat.admin.service;

import cn.wildfirechat.common.model.query.GroupMemberQuery;
import cn.wildfirechat.common.model.vo.GroupMemberPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class GroupMemberServiceTest {

    @Autowired
    private GroupMemberService groupMemberService;

//    @Test
    public void pageTest() {
        Page page = new Page();
        page.setPage(0);
        page.setRows(2);
        PageVO<GroupMemberPageVO> pageVO = groupMemberService.page(GroupMemberQuery.builder().groupId(1L).build(), page);
        log.info("输出: {}", pageVO);
    }

}
