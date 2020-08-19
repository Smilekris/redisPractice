package com.example.rpcconsumer.controller;

import com.example.rpcconsumer.service.impl.CreditConsumerServiceImpl;
import com.example.rpcprovider.rpccommon.entity.CreditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author kris
 * @Date 2020/6/9
 **/
@RestController
@RequestMapping("/rpc/dubbo/consumer")
public class TestController {

    @Autowired
    private CreditConsumerServiceImpl creditConsumerService;

    @GetMapping("/get")
    public CreditEntity get(){
        return creditConsumerService.providerCredit();
    }
}
