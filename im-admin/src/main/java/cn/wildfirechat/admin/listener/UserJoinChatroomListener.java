package cn.wildfirechat.admin.listener;

import cn.wildfirechat.admin.common.utils.RedisUtil;
import cn.wildfirechat.admin.service.SensitiveWordService;
import cn.wildfirechat.common.model.bo.SensitiveWordHitCreateBO;
import cn.wildfirechat.common.model.bo.UserJoinChatroomCreateBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UserJoinChatroomListener {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 监听用户聊天敏感词信息
     * @param context
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.queue.user.join.chatroom}", durable = "true"),
            exchange = @Exchange(value = "${mq.exchange.user.join.chatroom}", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.routing.user.join.chatroom}")
    )
    @Transactional
    public void userJoinChatroom(@Payload String context) throws Exception {
        log.info("userJoinChatroom context: {}", context);
        UserJoinChatroomCreateBO bo = new ObjectMapper().readValue(context, UserJoinChatroomCreateBO.class);
        StringBuilder sb = new StringBuilder().append("chatroomId_").append(bo.getChatroomId()).append(":").append("uid_").append(bo.getUserId());
        redisUtil.set(sb.toString(), bo.getOperationTime().toString(), 1L, TimeUnit.DAYS);
    }
}
