package com.example.rpcprovider.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rpcprovider.redis.entity.HighTecoEntity;

import java.util.List;

public interface HighTecoService extends IService<HighTecoEntity> {

    List<HighTecoEntity> getList();

    String getTalkingPoint(String key);
}
