package com.example.rpcprovider.redis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName HighTecoEntity
 * @Author kris
 * @Date 2020/5/15
 **/
@Data
@TableName("t_h_vistor")
public class HighTecoEntity{

    private Long id;
    private String ip;
    private String user;
    private Integer createTime;
    private Integer updateTime;
}
