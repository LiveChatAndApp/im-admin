package cn.wildfirechat.admin.listener;

import java.util.Map;

import cn.wildfirechat.common.model.dto.MemberDTO;
import cn.wildfirechat.common.model.query.MemberQuery;
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

import cn.wildfirechat.admin.service.MemberService;
import cn.wildfirechat.common.model.po.MemberPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserUpdateSendListener {

	@Autowired
	private MemberService memberService;

	/**
	 * 监听用户聊天发送信息
	 * 
	 * @param context
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${mq.queue.user.update.send}", durable = "true"), exchange = @Exchange(value = "${mq.exchange.user.update.send}", type = ExchangeTypes.TOPIC), key = "${mq.topic.routing.user.update.send}"))
	@Transactional
	public void userUpdateSend(@Payload String context) throws Exception {
		log.info("userUpdateSend context: {}", context);
		Map<String, Object> map = new ObjectMapper().readValue(context, Map.class);
		MemberPO po = MemberPO.builder().uid((String) map.get("uid")).nickName((String) map.get("nickName"))
				.gender((Integer) map.get("gender")).phone((String) map.get("phone"))
				.avatarUrl((String) map.get("avatar")).email((String) map.get("email")).build();
		MemberDTO memberDTO = memberService.list(MemberQuery.builder().uid(po.getUid()).build()).stream().findFirst().orElse(new MemberDTO());
		po.setId(memberDTO.getId());
		memberService.update(po);
	}

}
