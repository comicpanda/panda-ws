package com.comicpanda;

import com.comicpanda.service.RedisMessageReceiver;
import com.comicpanda.service.RedisMessagingDelegate;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Yoon
 */
@ConfigurationProperties(prefix="redis")
@Configuration
@Setter
public class RedisConfig {
    private String host;
    private String password;
    private Integer port;
    private String topicName;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jcf = new JedisConnectionFactory();
        jcf.setDatabase(0);
        jcf.setHostName(host);
        jcf.setPassword(System.getProperty("redisPassword", password));
        jcf.setPort(port);
        return jcf;
    }

    @Bean
    public RedisMessageListenerContainer container(final JedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer() {{
            setConnectionFactory(connectionFactory);
        }};
        container.addMessageListener(listenerAdapter(), new ChannelTopic(topicName));
        return container;
    }

    @Bean
    public RedisMessageReceiver redisMessageReceiver() {
        return new RedisMessageReceiver();
    }

    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver(), "pong");
    }

    @Bean
    public StringRedisTemplate template(JedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        StringRedisSerializer srs = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(srs);
        redisTemplate.setHashKeySerializer(srs);
        redisTemplate.setValueSerializer(srs);
        redisTemplate.setHashValueSerializer(srs);
        return redisTemplate;
    }

    @Bean
    public RedisMessagingDelegate messagingDelegate() {
        return new RedisMessagingDelegate();
    }
}
