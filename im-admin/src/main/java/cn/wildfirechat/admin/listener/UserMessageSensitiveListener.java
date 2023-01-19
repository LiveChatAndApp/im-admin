package cn.wildfirechat.admin.listener;

import java.util.Date;

import javax.annotation.Resource;

import cn.wildfirechat.admin.service.SensitiveWordService;
import cn.wildfirechat.common.model.bo.SensitiveWordHitCreateBO;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.config.RedisConfig;
import cn.wildfirechat.admin.service.MessageService;
import cn.wildfirechat.common.model.bo.MemberMessageSendBO;
import cn.wildfirechat.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserMessageSensitiveListener {
    @Resource
    private SensitiveWordService sensitiveWordService;

    /**
     * 监听用户聊天敏感词信息
     * @param context
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.queue.user.message.sensitive}", durable = "true"),
            exchange = @Exchange(value = "${mq.exchange.user.message.sensitive}", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.routing.user.message.sensitive}")
    )
    @Transactional
    public void userMessageSensitive(@Payload String context) throws Exception {
        log.info("userMessageSensitive context: {}", context);
        SensitiveWordHitCreateBO bo = new ObjectMapper().readValue(context, SensitiveWordHitCreateBO.class);
        sensitiveWordService.syncSWHit(bo);
    }
}
