package com.example.rpcprovider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.rpcprovider.rpccommon.entity.CreditEntity;
import com.example.rpcprovider.rpccommon.service.CreditService;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @ClassName CreditServiceImpl
 * @Author kris
 * @Date 2020/6/9
 **/
@Service
@Component
public class CreditServiceImpl implements CreditService {
    @Override
    public CreditEntity providerCredit() {
        int currentTime = (int)Instant.now().getEpochSecond();
        CreditEntity a = new CreditEntity();
        a.setUser("aa");
        a.setAmount(50);
        a.setCreateTime(currentTime);
        return a;
    }
}
