package com.example.redis.config;

import com.example.redis.entity.HighTecoEntity;
import com.example.redis.service.HighTecoService;
import com.example.redis.service.impl.RedisService;
import com.example.redis.taskpool.MunalTaskPool;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;
    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;
    @Autowired
    private HighTecoService highTecoService;

    public final static int SUB_TASK_NUM= 500000;
    @Override
    public void run(String... args) throws Exception {
//        redisTemplate.opsForValue().set("test","test",1, TimeUnit.MINUTES);
        int count = highTecoService.count();
        List<HighTecoEntity> list = highTecoService.list();
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), list.size());
        int batch = count/SUB_TASK_NUM;
//        CountDownLatch countDownLatch = new CountDownLatch(batch);
//        for(int i = 1;i <=batch;i++){
//            int rowsNum = i<batch?SUB_TASK_NUM:count-i*SUB_TASK_NUM;
//            int key  =i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    for(int j =0;j<rowsNum;j++){
//                        HighTecoEntity highTecoEntity = list.get((key - 1) + j);
//                        redisService.addByBloomFilter(bloomFilterHelper,highTecoEntity.getId()+"",highTecoEntity.getId()+"");
//                    }
//                    countDownLatch.countDown();
//                }
//            };
//            MunalTaskPool.getInstance().execute(runnable);
//        }
//        countDownLatch.await();
        for(int i = 0;i<1000000;i++){
            HighTecoEntity highTecoEntity = list.get(i);
            redisService.addByBloomFilter(bloomFilterHelper,highTecoEntity.getId()+"",highTecoEntity.getId()+"");
        }
        System.out.println("finish import");
    }
}
