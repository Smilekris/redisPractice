package com.example.rpcprovider.redis.controller;

import com.example.rpcprovider.redis.entity.HighTecoEntity;
import com.example.rpcprovider.redis.service.HighTecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName HighTechoConcurrencyController
 * @Author kris
 * @Date 2020/5/15
 **/
@RestController
@RequestMapping("/new-pants")
@Slf4j
public class HighTechoConcurrencyController {
    @Autowired
    private HighTecoService highTecoService;

    @GetMapping("/display")
    public List<HighTecoEntity> display(){
        log.info("启动 display 方法");
        return highTecoService.getList();
    }

    @PostMapping("/display/update")
    public String update(@RequestBody HighTecoEntity highTecoEntity){
        log.info("启动 update 方法");
        highTecoEntity.setUpdateTime((int)Instant.now().getEpochSecond());
        highTecoEntity.setUser("jmeter_test"+Instant.now().getEpochSecond());
        highTecoEntity.setIp("company-"+ UUID.randomUUID());
        highTecoEntity.setCreateTime(1589533597);
        highTecoService.updateById(highTecoEntity);
        return "ok";
    }
}
