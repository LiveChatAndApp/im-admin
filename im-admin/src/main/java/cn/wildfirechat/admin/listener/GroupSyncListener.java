package cn.wildfirechat.admin.listener;

import cn.wildfirechat.admin.service.GroupService;
import cn.wildfirechat.common.model.bo.GroupSyncBO;
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
public class GroupSyncListener {
    @Resource
    private GroupService groupService;

    /**
     * 监听群组同步信息
     * @param context
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.queue.group.sync}", durable = "true"),
            exchange = @Exchange(value = "${mq.exchange.group.sync}", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.routing.group.sync}")
    )
    @Transactional
    public void groupSync(@Payload String context) throws Exception {
        log.info("groupSync context: {}", context);
        GroupSyncBO bo = new ObjectMapper().readValue(context, GroupSyncBO.class);
        log.info("groupSync bo: {}", bo);
        groupService.syncGroup(bo);
    }
}
