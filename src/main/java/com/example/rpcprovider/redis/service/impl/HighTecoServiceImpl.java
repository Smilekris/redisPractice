package com.example.rpcprovider.redis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rpcprovider.redis.entity.HighTecoEntity;
import com.example.rpcprovider.redis.mapper.HighTecoMapper;
import com.example.rpcprovider.redis.service.HighTecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HighTecoServiceImpl
 * @Author kris
 * @Date 2020/5/15
 **/
@Service
@Slf4j
public class HighTecoServiceImpl extends ServiceImpl<HighTecoMapper, HighTecoEntity> implements HighTecoService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<HighTecoEntity> getList() {
//        redisTemplate.opsForValue().set("test","test",1, TimeUnit.MINUTES);
        QueryWrapper<HighTecoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id",50);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public String getTalkingPoint(String key) {
        String talkingPointValue = (String)redisTemplate.opsForValue().get(key);
        if(talkingPointValue ==null){
            //互斥锁
            if(redisTemplate.opsForValue().setIfAbsent(key+"_mutex","key_mutex",3,TimeUnit.MINUTES)){
                //从数据库查询
                HighTecoEntity byId = this.getById(1);
                redisTemplate.opsForValue().set(key,byId.getUser(),3,TimeUnit.MINUTES);
                return byId.getUser();
            }else{
                //拿不到互斥锁，休眠一秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.info("获取失败");
                    return null;
                }
                String getTalkingValueByRedis = (String) redisTemplate.opsForValue().get(key);
                return null;
            }
        }
        return talkingPointValue;
    }


}
