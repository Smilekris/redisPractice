package com.example.rpcconsumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.rpcprovider.rpccommon.entity.CreditEntity;
import com.example.rpcprovider.rpccommon.service.CreditService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @ClassName CreditConsumerServiceImpl
 * @Author kris
 * @Date 2020/6/9
 **/
@Service
public class CreditConsumerServiceImpl implements CreditService {
    @Reference
    private CreditService cr1editService ;
    @Override
    public CreditEntity providerCredit() {
        return cr1editService.providerCredit();
    }
}
