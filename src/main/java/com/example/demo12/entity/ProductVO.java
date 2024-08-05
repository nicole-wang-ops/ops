package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductVO extends Product {

    public BigDecimal rate;

    public Integer pid;

    public Integer buyId;

    public BigDecimal businessRate;

    public String businessContent;






}
