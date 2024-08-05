package com.example.demo12.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;



//redis
@Configuration
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class}) // 注意exclude
public class RedisCache {


    @Value("${ad.host}")
    String adContentHost;
    @Value("${ad.port}")
    int adContentPort;
    @Value("${ad.database}")
    int adContentDatabase;


    @Bean("redisConnection")
    public LettuceConnectionFactory adRedisConnection() {
        RedisStandaloneConfiguration server = new RedisStandaloneConfiguration();
        server.setHostName(adContentHost);
        server.setDatabase(adContentDatabase);
        server.setPort(adContentPort);
        return new LettuceConnectionFactory(server);
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, String> adRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(adRedisConnection());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置完这个可以直接将对象以json格式存入redis中，但是取出来的时候要用JSON.parseArray(Json.toJsonString(object),Object.class)解析一下
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        //调用后完成设置
        redisTemplate.afterPropertiesSet();
        return redisTemplate;


    }

}
