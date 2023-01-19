package cn.wildfirechat.admin.job;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.config.RedisConfig;
import cn.wildfirechat.admin.mapper.GroupMapper;
import cn.wildfirechat.admin.mapper.MainPageInfoMapper;
import cn.wildfirechat.admin.mapper.MemberMapper;
import cn.wildfirechat.admin.mapper.MessageMapper;
import cn.wildfirechat.common.model.po.GroupPO;
import cn.wildfirechat.common.model.po.MainPageInfoPO;
import cn.wildfirechat.common.model.po.MemberPO;
import cn.wildfirechat.common.model.vo.ActiveGroupVO;
import cn.wildfirechat.common.model.vo.ActiveMemberVO;
import cn.wildfirechat.common.model.vo.MainPageInfoVO;
import cn.wildfirechat.common.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SaveMainPageInfoRecordJob {
    private static final String logPrefix = "【每日更新首页资讯历史信习】";

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private MainPageInfoMapper mainPageInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ObjectMapper objectMapper;




//    @Scheduled(cron = "0 30 23 * * ?")
    @Scheduled(fixedRate = 1 * TimeUnit.HOUR)
    public void saveMainPageInfoRecord() {
        try {
            log.info("「Job」{} SaveMainPageInfoRecordJob.saveMainPageInfoRecord Start", logPrefix);
            MainPageInfoVO mainPageInfo = getMainPageInfo();
            MainPageInfoPO po = MainPageInfoPO.builder().build();
            BeanUtils.copyProperties(mainPageInfo, po, "top10ActiveMember", "top10ActiveGroup");

            po.setTop10ActiveMember(objectMapper.writeValueAsString(mainPageInfo.getTop10ActiveMember()));
            po.setTop10ActiveGroup(objectMapper.writeValueAsString(mainPageInfo.getTop10ActiveGroup()));

            MainPageInfoPO todayInfo = mainPageInfoMapper.find(DateUtils.getNow("yyyy-MM-dd"));
            if(null == todayInfo){
                mainPageInfoMapper.insert(po);
                log.info("「Job」{} 写入今日资料 完成", logPrefix);
            }else{
                po.setId(todayInfo.getId());
                mainPageInfoMapper.update(po);
                log.info("「Job」{} 更新今日资料 完成", logPrefix);
            }

        } catch (Exception e) {
            log.info("「Job」{} 发生异常", logPrefix, e);
        }
        log.info("「Job」{} SaveMainPageInfoRecordJob.saveMainPageInfoRecord End", logPrefix);
    }

    private MainPageInfoVO getMainPageInfo() {
        Long activeMemberCount      		    = redisUtil.getZSetCount(RedisConfig.ACTIVE_MEMBER_KEY);//活跃用户数
        Set<String> activeMember                = redisUtil.getRevZSet(RedisConfig.ACTIVE_MEMBER_KEY, Double.MIN_VALUE, Double.MAX_VALUE, 0, 10);//Top10活跃用户数 <uid, score>
        Set<String> activeGroup                 = redisUtil.getRevZSet(RedisConfig.ACTIVE_GROUP_KEY, Double.MIN_VALUE, Double.MAX_VALUE, 0, 10);//Top10活跃群组 <gid, score>
//        String addMemberCountStr           		= (String) redisUtil.get(RedisConfig.ADD_MEMBER_COUNT_CURR_DATE_KEY);//新增用户数
//        String addGroupCountStr           		= (String) redisUtil.get(RedisConfig.ADD_GROUP_COUNT_CURR_DATE_KEY);//新建群个数
//        String messageCountStr                	= (String) redisUtil.get(RedisConfig.MESSAGE_COUNT_CURR_DATE_KEY);//发送消息数
//        Integer addMemberCount      = addMemberCountStr == null? 0:Integer.valueOf(addMemberCountStr);
//        Integer addGroupCount       = addGroupCountStr == null? 0:Integer.valueOf(addGroupCountStr);
//        Long messageCount           = messageCountStr == null? 0L:Long.valueOf(messageCountStr);

        Integer addMemberCount      = memberMapper.countCreateAtCurrDate();//新增用户数
        Integer addGroupCount       = groupMapper.countCreateAtCurrDate();//新建群个数
        Long messageCount           = messageMapper.countCreateAtCurrDate();//发送消息数

        AtomicInteger memberIndex = new AtomicInteger(1);
        Set<ActiveMemberVO> top10ActiveMember = activeMember.stream().map(uid -> {
//            log.info("top10ActiveMember uid:{}", uid);
            MemberPO memberPO = memberMapper.selectByUid(uid);
            if (memberPO == null) { return null; }
            return ActiveMemberVO.builder()
                    .memberName(memberPO.getMemberName())
                    .nickName(memberPO.getNickName())
                    .range(memberIndex.getAndIncrement())
                    .messageCount(redisUtil.getZSetScore(RedisConfig.ACTIVE_MEMBER_KEY, uid).longValue()).build();
        }).filter(Objects::nonNull).collect(Collectors.toSet());

        AtomicInteger groupIndex = new AtomicInteger(1);
        List<ActiveGroupVO> top10ActiveGroup = activeGroup.stream().map(gid -> {
            GroupPO groupPO = groupMapper.selectByGid(gid);
            return ActiveGroupVO.builder()
                    .gid(gid)
                    .name(groupPO.getName())
                    .range(groupIndex.getAndIncrement())
                    .messageCount(redisUtil.getZSetScore(RedisConfig.ACTIVE_GROUP_KEY, gid).longValue()).build();
        }).collect(Collectors.toList());


        Integer memberGrandTotalCount = memberMapper.count();//当前系统总用户数
        Integer groupGrandTotalCount = groupMapper.count();//当前系统群总数
        log.info("获取结果值为: activeMemberCount:{}, addMemberCount:{}, addGroupCount:{}, messageCount:{}, memberGrandTotalCount:{}, groupGrandTotalCount:{}",
                activeMemberCount, addMemberCount, addGroupCount, messageCount, memberGrandTotalCount, groupGrandTotalCount);

        MainPageInfoVO result = MainPageInfoVO.builder()
                .activeMemberCount(activeMemberCount)
                .addMemberCount(addMemberCount)
                .addGroupCount(addGroupCount)
                .messageCount(messageCount)
                .top10ActiveMember(top10ActiveMember)
                .top10ActiveGroup(top10ActiveGroup)
                .memberGrandTotalCount(memberGrandTotalCount)
                .groupGrandTotalCount(groupGrandTotalCount)
                .build();

        return result;
    }

}
