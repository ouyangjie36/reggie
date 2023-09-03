package com.jGod.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        testRedis();
    }

    public void testRedis(){
        Set<String> set = redisTemplate.keys("*");
        for (String s :set) {
            System.out.println(s);
        }
        System.out.println("----");
        System.out.println(redisTemplate.hasKey("key2"));
        System.out.println("----");
        System.out.println(redisTemplate.hasKey("key5"));
        System.out.println("----");
        System.out.println(redisTemplate.delete("key1"));
        System.out.println("----");
        System.out.println(redisTemplate.type("key2"));
    }

}
