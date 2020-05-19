package com.example.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.redis.entity.HighTecoEntity;

import java.util.List;

public interface HighTecoService extends IService<HighTecoEntity> {

    List<HighTecoEntity> getList();
}
