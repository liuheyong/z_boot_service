package com.boot.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.boot.commons.dto.ECooperateMer;
import com.boot.commons.response.QueryECooperateMerResponse;
import com.boot.commons.service.ECooperateMerService;
import com.boot.service.mapper.ECooperateMerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: LiuHeYong
 * @create: 2019-05-27
 * @description:
 **/
@Component
@Service(interfaceClass = ECooperateMerService.class, version = "1.0.0", timeout = 10000)
public class ECooperateMerServiceImpl implements ECooperateMerService {

    private static final Logger logger = LoggerFactory.getLogger(ECooperateMerServiceImpl.class);

    @Autowired
    private ECooperateMerMapper eCooperateMerMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ECooperateMer queryECooperateMerInfo(ECooperateMer eCooperateMer) {
        Optional<ECooperateMer> optDto = Optional.ofNullable(Optional.ofNullable(eCooperateMerMapper.selectECooperateMerInfo(eCooperateMer)).orElseGet(ECooperateMer::new));
        return optDto.get();
    }

    @Override
    public QueryECooperateMerResponse queryECooperateMerListPage(ECooperateMer eCooperateMer) throws Exception {
        QueryECooperateMerResponse response = null;
        try {
            //使用String格式化Redis的key 否则Key的可读性并不好！
            RedisSerializer redisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(redisSerializer);
            List<ECooperateMer> eList = null;
            //从Redis中获取数据根据key
            eList = (List<ECooperateMer>) redisTemplate.opsForValue().get("eCooperateMerList");
            //使用双重检查解决缓存穿透问题
            if (eList == null) {//判断list是否为空如是空表示为第一批并发请求
                synchronized (this) {//让第一批的所有并发请求排队执行
                    //二次从Redis中获取数据
                    eList = (List<ECooperateMer>) redisTemplate.opsForValue().get("eCooperateMerList");
                    if (eList == null) {//判断list是否为空如果为空表示为第一批并发请求中的第一个
                        eList = eCooperateMerMapper.queryECooperateMerListPage();
                        redisTemplate.opsForValue().set("eCooperateMerList", eList, 30, TimeUnit.SECONDS);
                        logger.info("从数据库中获取的数据");
                    } else {
                        logger.info("从缓存中获取的数据");
                    }
                }
            } else {
                logger.info("从缓存中获取的数据");
            }
            response.seteCooperateMerList(eList);
            return response;
        } catch (Exception e) {
            logger.error("系统异常", e);
            throw new Exception("系统异常");
        }
    }
}
