package com.example.rpcprovider.rpccommon.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CreditEntity
 * @Author kris
 * @Date 2020/6/9
 **/
@Data
public class CreditEntity implements Serializable {

    private String user;
    private Integer amount;
    private Integer createTime;
}
