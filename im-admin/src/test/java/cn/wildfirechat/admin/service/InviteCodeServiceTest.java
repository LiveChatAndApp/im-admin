package cn.wildfirechat.admin.service;

import cn.wildfirechat.common.model.query.InviteCodePageQuery;
import cn.wildfirechat.common.model.vo.InviteCodeDefaultMemberPageVO;
import cn.wildfirechat.common.model.vo.InviteCodePageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class InviteCodeServiceTest {

    @Autowired
    private InviteCodeService inviteCodeService;

//    @Test
    public void pageTest() {
        Page page = new Page();
        page.setPage(0);
        page.setRows(2);
        PageVO<InviteCodePageVO> inviteCodePageVOPageVO = inviteCodeService.page(new InviteCodePageQuery(), page);
        log.info("输出: {}", inviteCodePageVOPageVO);
    }
}
