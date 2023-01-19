package cn.wildfirechat.admin.listener;

import cn.wildfirechat.admin.service.GroupMemberService;
import cn.wildfirechat.common.model.bo.GroupMemberSyncBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Component
public class GroupMemberSyncListener {
    @Resource
    private GroupMemberService groupMemberService;

    /**
     * 监听群组會員同步信息
     * @param context
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.queue.group.member.sync}", durable = "true"),
            exchange = @Exchange(value = "${mq.exchange.group.member.sync}", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.routing.group.member.sync}")
    )
    @Transactional
    public void groupMemberSync(@Payload String context) throws Exception {
        log.info("groupMemberSync context: {}", context);
        GroupMemberSyncBO bo = new ObjectMapper().readValue(context, GroupMemberSyncBO.class);
        log.info("groupMemberSync bo: {}", bo);
        boolean isGroupDelay = groupMemberService.syncGroupMember(bo);
        log.info("groupMemberSync 因群组数据缓慢 需等待三秒重试: {}", isGroupDelay);
        if (isGroupDelay) {
            Thread.sleep(3000); // 暂停三秒
            new Thread(()->{
                log.info("groupMemberSync 因群组数据缓慢重新再一次");
                boolean isGroupSubDelay = groupMemberService.syncGroupMember(bo);
                log.info("groupMemberSync isGroupSubDelay: {}", isGroupSubDelay);
            }).start();
        }
    }
}
