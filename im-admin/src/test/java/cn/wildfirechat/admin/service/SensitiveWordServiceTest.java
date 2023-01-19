package cn.wildfirechat.admin.service;

import cn.wildfirechat.admin.mapper.SensitiveWordHitMapper;
import cn.wildfirechat.common.model.bo.SensitiveWordAddBO;
import cn.wildfirechat.common.model.po.SensitiveWordHitPO;
import cn.wildfirechat.common.model.query.GroupMemberQuery;
import cn.wildfirechat.common.model.vo.GroupMemberPageVO;
import cn.wildfirechat.common.model.vo.PageVO;
import cn.wildfirechat.common.support.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
class SensitiveWordServiceTest {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private SensitiveWordHitMapper sensitiveWordHitMapper;

//    @Test
    public void insertSWTest() {
        List<String> strings = Arrays.asList("qwe","asd","zxc","ert");
        List<SensitiveWordAddBO> list = new ArrayList<>();
        strings.forEach(s->{
            SensitiveWordAddBO bo = SensitiveWordAddBO.builder().content(s).build();
            list.add(bo);
        });
        sensitiveWordService.swInsertBatch(list);
    }

//    @Test
    public void insertSWHitTest() {
        List<String> strings = Arrays.asList("qweqwe","asdasd","zxczxc","ertert");
        strings.forEach(s ->{
            System.out.println("塞值为:"+s);
            SensitiveWordHitPO record = SensitiveWordHitPO.builder()
                    .senderId(60L)
                    .sender("kygqmws2k")
                    .receiverId(61L)
                    .receiver("dygqmws2k")
                    .chatType(1)
                    .content(s)
                    .creator("admin")
                    .build();
            sensitiveWordHitMapper.insert(record);
        });



    }

}
