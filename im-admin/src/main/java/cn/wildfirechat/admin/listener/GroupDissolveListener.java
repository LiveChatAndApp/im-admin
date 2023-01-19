package cn.wildfirechat.admin.listener;

import cn.wildfirechat.admin.service.GroupService;
import cn.wildfirechat.common.model.bo.GroupDissolveBO;
import cn.wildfirechat.common.model.bo.GroupSyncBO;
import cn.wildfirechat.common.model.enums.EditorRoleEnum;
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
public class GroupDissolveListener {
    @Resource
    private GroupService groupService;

    /**
     * 监听群组解散信息
     * @param context
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.queue.group.dissolve}", durable = "true"),
            exchange = @Exchange(value = "${mq.exchange.group.dissolve}", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.routing.group.dissolve}")
    )
    @Transactional
    public void groupDissolve(@Payload String context) throws Exception {
        log.info("groupDissolve context: {}", context);
        GroupDissolveBO bo = new ObjectMapper().readValue(context, GroupDissolveBO.class);
        log.info("groupDissolve bo: {}", bo);
        groupService.dissolveGroup(bo, EditorRoleEnum.MEMBER);
    }
}
