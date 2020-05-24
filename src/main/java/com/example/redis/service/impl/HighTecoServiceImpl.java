package com.example.redis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redis.entity.HighTecoEntity;
import com.example.redis.mapper.HighTecoMapper;
import com.example.redis.service.HighTecoService;
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
}
