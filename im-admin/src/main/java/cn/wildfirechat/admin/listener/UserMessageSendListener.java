package cn.wildfirechat.admin.listener;

import java.util.Date;
import java.util.stream.Stream;

import javax.annotation.Resource;

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
public class UserMessageSendListener {
	@Resource
	private MessageService messageService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 监听用户聊天发送信息
	 * 
	 * @param context
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${mq.queue.user.message.send}", durable = "true"), exchange = @Exchange(value = "${mq.exchange.user.message.send}", type = ExchangeTypes.TOPIC), key = "${mq.topic.routing.user.message.send}"))
	@Transactional
	public void userMessageSend(@Payload String context) throws Exception {
//		log.info("userMessageSend context: {}", context);
		MemberMessageSendBO bo = new ObjectMapper().readValue(context, MemberMessageSendBO.class);
		log.info("userMessageSend from: {}, to: {}, searchableContent: {}", bo.getFrom(), bo.getTo(), bo.getMessage().getSearchableContent());
		messageService.synMessage(bo);

		// redis记录首页相关资讯
		if (bo.isGroup()) {
			// 群
			redisUtil.setIncrZSet(RedisConfig.ACTIVE_GROUP_KEY, bo.getTarget(), 1D);
			redisUtil.setExpireAt(RedisConfig.ACTIVE_GROUP_KEY, DateUtils.getTailDay(new Date()));
		}
		if (Stream.of("admin", "PrettyRobot").noneMatch(s -> s.equals(bo.getFrom()))) {
			if (bo.getContentType() > 0 && bo.getContentType() < 10) {
				redisUtil.setIncrZSet(RedisConfig.ACTIVE_MEMBER_KEY, bo.getFrom(), 1D);
				redisUtil.setExpireAt(RedisConfig.ACTIVE_MEMBER_KEY, DateUtils.getTailDay(new Date()));
			}

			// 除非有实时需求,否则改用DB查询就好
			// redisUtil.increment(RedisConfig.MESSAGE_COUNT_CURR_DATE_KEY, 1);
			// redisUtil.setExpireAt(RedisConfig.MESSAGE_COUNT_CURR_DATE_KEY,
			// DateUtils.getTailDay(new Date()));
		}

	}

}
