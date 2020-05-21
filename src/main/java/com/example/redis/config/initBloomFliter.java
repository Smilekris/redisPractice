package com.example.redis.config;

import com.example.redis.entity.HighTecoEntity;
import com.example.redis.service.HighTecoService;
import com.example.redis.service.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName initBloomFliter
 * @Author kris
 * @Date 2020/5/21
 **/
@Component
public class initBloomFliter implements CommandLineRunner {
    @Autowired
    private RedisService redisService;
    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;
    @Autowired
    private HighTecoService highTecoService;

    public final static int SUB_TASK_NUM= 500000;
    @Override
    public void run(String... args) throws Exception {
        int count = highTecoService.count();
        List<HighTecoEntity> list = highTecoService.list();
        int batch = count/SUB_TASK_NUM;
        CountDownLatch countDownLatch = new CountDownLatch(batch);
        for(int i = 1;i <=batch;i++){
            int rowsNum = i<batch?SUB_TASK_NUM:count-i*SUB_TASK_NUM;
            int key  =i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for(int j =0;j<rowsNum;j++){
                        HighTecoEntity highTecoEntity = list.get((key - 1) + j);
                        redisService.addByBloomFilter();
                    }
                }
            };
            ;
        }
        redisService.addByBloomFilter(bloomFilterHelper,);
    }
}
