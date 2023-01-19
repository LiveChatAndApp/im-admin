package cn.wildfirechat.admin.listener;

import java.util.Objects;

import javax.annotation.Resource;

import cn.wildfirechat.common.model.enums.RelateVerifyEnum;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wildfirechat.admin.service.FriendService;
import cn.wildfirechat.common.model.bo.MemberJoinFriendBO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserFriendSyncListener {
	@Resource
	private FriendService friendService;

	/**
	 * 监听用户好友关系变更
	 * 
	 * @param context
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${mq.queue.user.friend.sync}", durable = "true"), exchange = @Exchange(value = "${mq.exchange.user.friend.sync}", type = ExchangeTypes.TOPIC), key = "${mq.topic.routing.user.friend.sync}"))
	@Transactional
	public void userMessageSend(@Payload String context) throws Exception {
		log.info("[IM][RabbitMQ]UserFriendSyncListener context: {}", context);
		MemberJoinFriendBO bo = new ObjectMapper().readValue(context, MemberJoinFriendBO.class);

		if (Objects.equals(bo.getAction(), MemberJoinFriendBO.ADD_FRIEND)) {
			bo.setVerify(RelateVerifyEnum.SUCCESS.getValue());
			friendService.insert(bo);
		} else if (Objects.equals(bo.getAction(), MemberJoinFriendBO.BLOCK_FRIEND)) {
			friendService.update(bo);
		} else if(Objects.equals(bo.getAction(), MemberJoinFriendBO.DELETE_FRIEND)){
			bo.setVerify(RelateVerifyEnum.DELETED.getValue());
			friendService.update(bo);
		}
		log.info("[IM][RabbitMQ]UserFriendSyncListener bo: {}", bo);
	}
}