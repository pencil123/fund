package cn.blogscn.fund.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    private final static Logger logger = LoggerFactory.getLogger(RabbitmqConfig.class);
    @Value("${rabbitmq.process.queue}")
    private String queue;
    @Value("${rabbitmq.process.exchange}")
    private String exchange;
    @Value("${rabbitmq.process.routing}")
    private String routing;
    @Value("${rabbitmq.dlx.queue}")
    private String dlxQueue;
    @Value("${rabbitmq.dlx.exchange}")
    private String dlxExchange;
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            if (ack) {
                logger.info("消息发送成功；时间：{}", System.currentTimeMillis());
            } else {
                logger.error("消息发送失败，原因：{}", cause);
            }
        };
    }

    @Bean
    public RabbitTemplate.ReturnsCallback returnsCallback() {
        return (returnedMessage) -> {
            logger.error("发送失败，请检测路由：{}", returnedMessage.getMessage());
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setConfirmCallback(confirmCallback());
        rabbitTemplate.setReturnsCallback(returnsCallback());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public Queue DirectDlxQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //return new Queue("TestDirectQueue",true,true,false);
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(dlxQueue, true, false, false);
    }

    @Bean
    DirectExchange DirectDlxExchange() {
        return new DirectExchange(dlxExchange, true, false);
    }

    @Bean
    Binding bindingDlxDirect() {
        return BindingBuilder.bind(DirectDlxQueue()).to(DirectDlxExchange()).with(routing);
    }

    @Bean
    public Queue DirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //return new Queue("TestDirectQueue",true,true,false);
        //一般设置一下队列的持久化就好,其余两个就是默认false
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        args.put("x-dead-letter-exchange", dlxExchange);
        args.put("x-dead-letter-routing-key", routing);
        return new Queue(queue, true, false, false, args);
    }

    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange DirectExchange() {
        return new DirectExchange(exchange, true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(DirectQueue()).to(DirectExchange()).with(routing);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息在传输中的格式，这里采用 JSON 的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置并发消费者实例的初始数量为1
        factory.setConcurrentConsumers(1);
        //设置并发消费者实例中最大数量为1
        factory.setMaxConcurrentConsumers(1);
        //设置并发消费者实例中每个实例拉取的消息数量为1个
        factory.setPrefetchCount(1);
        return factory;
    }
}

