package edu.vn.iuh.fit.cartservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//cấu hình cho spring-data-redis sử dụng lettuce kết nối tới Redis.
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Tạo Standalone Connection tới Redis
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

//    template là cách write/read Redis bằng Java thay vì viết trực tiếp
//    truyền connection vào để nó biết đang kết nối tới redis (host, port)
    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // tạo ra một RedisTemplate
        // Với Key là Object
        // Value là Object
        // RedisTemplate giúp chúng ta thao tác với Redis
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 1. Cấu hình Serializer cho Key (biến chuỗi Java thành chuỗi thường trong Redis)
        template.setKeySerializer(new StringRedisSerializer());

        // 2. Cấu hình Serializer cho HashKey (biến ProductId thành chuỗi thường)
        template.setHashKeySerializer(new StringRedisSerializer());

        // 3. Cấu hình Serializer cho Value (để in ra con số dễ dàng)
        template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));

        template.afterPropertiesSet();
        return template;
    }
}

/**
 CONCLUDE:

application.yml
      ↓
RedisConnectionFactory (tạo connection)
      ↓
RedisTemplate (dùng connection đó)
      ↓
Service của bạn
**/

//delete all: FLUSHALL
//get all: KEYS *

