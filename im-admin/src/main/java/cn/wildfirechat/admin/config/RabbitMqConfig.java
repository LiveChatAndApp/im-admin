package cn.wildfirechat.admin.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @NacosValue(value = "${mq.queue.user.message.send}", autoRefreshed = true)
    private String userMessageSendQueue;
    @NacosValue(value = "${mq.topic.routing.user.message.send}", autoRefreshed = true)
    private String userMessageSendTopicRouting;
    @NacosValue(value = "${mq.exchange.user.message.send}", autoRefreshed = true)
    private String userMessageSendExchange;
    @NacosValue(value = "${mq.queue.user.update.send}", autoRefreshed = true)
    private String userUpdateSendQueue;
    @NacosValue(value = "${mq.topic.routing.user.update.send}", autoRefreshed = true)
    private String userUpdateSendTopicRouting;
    @NacosValue(value = "${mq.exchange.user.update.send}", autoRefreshed = true)
    private String userUpdateSendExchange;
    @NacosValue(value = "${mq.queue.user.friend.sync}", autoRefreshed = true)
    private String userFriendSyncQueue;
    @NacosValue(value = "${mq.topic.routing.user.friend.sync}", autoRefreshed = true)
    private String userFriendSyncTopicRouting;
    @NacosValue(value = "${mq.exchange.user.friend.sync}", autoRefreshed = true)
    private String userFriendSyncExchange;
    @NacosValue(value = "${mq.queue.group.sync}", autoRefreshed = true)
    private String groupSyncQueue;
    @NacosValue(value = "${mq.topic.routing.group.sync}", autoRefreshed = true)
    private String groupSyncTopicRouting;
    @NacosValue(value = "${mq.exchange.group.sync}", autoRefreshed = true)
    private String groupSyncExchange;

    @NacosValue(value = "${mq.queue.group.member.sync}", autoRefreshed = true)
    private String groupMemberSyncQueue;
    @NacosValue(value = "${mq.topic.routing.group.member.sync}", autoRefreshed = true)
    private String groupMemberSyncTopicRouting;
    @NacosValue(value = "${mq.exchange.group.member.sync}", autoRefreshed = true)
    private String groupMemberSyncExchange;

    @NacosValue(value = "${mq.queue.user.message.sensitive}", autoRefreshed = true)
    private String userMessageSensitiveQueue;
    @NacosValue(value = "${mq.topic.routing.user.message.sensitive}", autoRefreshed = true)
    private String userMessageSensitiveTopicRouting;
    @NacosValue(value = "${mq.exchange.user.message.sensitive}", autoRefreshed = true)
    private String userMessageSensitiveExchange;

    @NacosValue(value = "${mq.queue.user.join.chatroom}", autoRefreshed = true)
    private String userJoinChatroomQueue;
    @NacosValue(value = "${mq.topic.routing.user.join.chatroom}", autoRefreshed = true)
    private String userJoinChatroomTopicRouting;
    @NacosValue(value = "${mq.exchange.user.join.chatroom}", autoRefreshed = true)
    private String userJoinChatroomExchange;

    @NacosValue(value = "${spring.rabbitmq.host}", autoRefreshed = true)
    private String address;
    @NacosValue(value = "${spring.rabbitmq.username}", autoRefreshed = true)
    private String username;
    @NacosValue(value = "${spring.rabbitmq.password}", autoRefreshed = true)
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(address);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * 设置交换器的名称
     * @return TopicExchange
     */
    @Bean
    public TopicExchange userMessageSendExchange() {
        return new TopicExchange(userMessageSendExchange);
    }
    @Bean
    public TopicExchange userUpdateSendExchange() {
        return new TopicExchange(userUpdateSendExchange);
    }
    @Bean
    public TopicExchange groupSyncExchange() {
        return new TopicExchange(groupSyncExchange);
    }
    @Bean
    public TopicExchange userFriendSyncExchange() {
        return new TopicExchange(userFriendSyncExchange);
    }
    @Bean
    public TopicExchange groupMemberSyncExchange() {
        return new TopicExchange(groupMemberSyncExchange);
    }
    @Bean
    public TopicExchange userMessageSensitiveExchange() {
        return new TopicExchange(userMessageSensitiveExchange);
    }
    @Bean
    public TopicExchange userJoinChatroomExchange() {
        return new TopicExchange(userJoinChatroomExchange);
    }

    /**
     * 队列名称
     * @return Queue
     */
    @Bean
    public Queue userMessageSendQueueMessage() {
        return new Queue(userMessageSendQueue);
    }
    @Bean
    public Queue userUpdateSendQueueMessage() {
        return new Queue(userUpdateSendQueue);
    }
    @Bean
    public Queue groupSyncQueueMessage() {
        return new Queue(groupSyncQueue);
    }
    @Bean
    public Queue userFriendSyncQueueMessage() {
        return new Queue(userFriendSyncQueue);
    }
    @Bean
    public Queue groupMemberSyncQueueMessage() {
        return new Queue(groupMemberSyncQueue);
    }
    @Bean
    public Queue userMessageSensitiveQueueMessage() {
        return new Queue(userMessageSensitiveQueue);
    }
    @Bean
    public Queue userJoinChatroomQueueMessage() {
        return new Queue(userJoinChatroomQueue);
    }

    /**
     * 将指定routing key的名称绑定交换器的队列
     */
    @Bean
    public Binding bindingUserMessageSendExchangeMessage(Queue userMessageSendQueueMessage, TopicExchange userMessageSendExchange) {
        return BindingBuilder.bind(userMessageSendQueueMessage).to(userMessageSendExchange).with(userMessageSendTopicRouting);
    }
    @Bean
    public Binding bindingUserUpdateSendExchangeMessage(Queue userUpdateSendQueueMessage, TopicExchange userUpdateSendExchange) {
        return BindingBuilder.bind(userUpdateSendQueueMessage).to(userUpdateSendExchange).with(userUpdateSendTopicRouting);
    }
    @Bean
    public Binding bindingGroupSyncExchangeMessage(Queue groupSyncQueueMessage, TopicExchange groupSyncExchange) {
        return BindingBuilder.bind(groupSyncQueueMessage).to(groupSyncExchange).with(groupSyncTopicRouting);
    }
    @Bean
    public Binding bindingUserFriendSyncExchangeMessage(Queue userFriendSyncQueueMessage, TopicExchange userFriendSyncExchange) {
        return BindingBuilder.bind(userFriendSyncQueueMessage).to(userFriendSyncExchange).with(userFriendSyncTopicRouting);
    }
    @Bean
    public Binding bindingGroupMemberSyncExchangeMessage(Queue groupMemberSyncQueueMessage, TopicExchange groupMemberSyncExchange) {
        return BindingBuilder.bind(groupMemberSyncQueueMessage).to(groupMemberSyncExchange).with(groupMemberSyncTopicRouting);
    }
    @Bean
    public Binding bindingUserMessageSensitiveExchangeMessage(Queue userMessageSensitiveQueueMessage, TopicExchange userMessageSensitiveExchange) {
        return BindingBuilder.bind(userMessageSensitiveQueueMessage).to(userMessageSensitiveExchange).with(userMessageSensitiveTopicRouting);
    }
    @Bean
    public Binding bindingUserJoinChatroomExchangeMessage(Queue userJoinChatroomQueueMessage, TopicExchange userJoinChatroomExchange) {
        return BindingBuilder.bind(userJoinChatroomQueueMessage).to(userJoinChatroomExchange).with(userJoinChatroomTopicRouting);
    }
}
