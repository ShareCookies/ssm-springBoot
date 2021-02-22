package com.cloud.spring.demo.service.impl;

import com.cloud.spring.demo.service.RedisTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Package: com.cloud.spring.demo.service.impl
 * @ClassName: RedisTestServiceImpl
 * @Author: zhougaoyang
 * @Description: redis服务测试接口实现
 * @Date: 2019/4/27 10:16
 * @Version: 1.0
 */
@Service
public class StringRedisTestServiceImpl implements RedisTestService {
    private static final Logger log = LoggerFactory.getLogger(RedisTestService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * @Description 获取String类型的value
     * @param name
     * @return
     */
    @Override
    public String findName(String name) {
        if (name==null){
            log.error("===============key为null======================================================");
        }
        return redisTemplate.opsForValue().get(name);
    }

    /**
     * @Description 添加String类型的key-value
     * @param name
     * @param value
     * @return
     */
    @Override
    public String setNameValue(String name, String value) {
        log.info("==================添加String类型的key-value========================================");
        redisTemplate.opsForValue().set(name,value);
        return name;
    }

    /**
     * @Description 根据key删除redis的数据
     * @param name
     * @return
     */
    @Override
    public String delNameValue(String name) {
        redisTemplate.delete(name);
        return name;
    }

    /**
     * @Description 根据key获取list类型的value(范围)
     * @param key
     * @return
     */
    @Override
    public List<String> findList(String key,int start,int end) {
        log.info("=====================按照范围查询redis中List类型=======================================");
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * @Description 插入多条数据
     * @param key
     * @param value
     * @return
     */
    @Override
    public long setList(String key, List<String> value) {
        log.info("=========================redis List type insert ======================================");
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * @Description 获取list最新记录（右侧）
     * @param key
     * @return
     */
    @Override
    public String findLatest(String key) {
        log.info("=============================rides List latest rigth==================================");
        return redisTemplate.opsForList().index(key,redisTemplate.opsForList().size(key)-1);
    }

    /**
     * @Description 查询hash
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> findHash(String key) {
        log.info("===================================redis hash =========================================");
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @Description 查询hash中所有的key
     * @param key
     * @return
     */
    @Override
    public Set<Object> findHashKeys(String key) {
        log.info("====================================== All keys of hash ===============================");
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * @Description 查询hash中所有的value
     * @param key
     * @return
     */
    @Override
    public List<Object> findHashValues(String key) {
        log.info("===================================== All values of hash ==============================");
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * @Desscription 插入hash数据
     * @param key
     * @param map
     * @return
     */
    @Override
    public long insertHash(String key, Map<String, Object> map) {
        log.info("====================================== insert hashes into redis ========================");
        redisTemplate.opsForHash().putAll(key,map);
        return map.size();
    }
}