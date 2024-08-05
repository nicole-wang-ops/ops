package com.example.demo12.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.*;
import com.example.demo12.mapper.AdMapper;
import com.example.demo12.mapper.MessageMapper;
import com.example.demo12.mapper.ProductMapper;
import com.example.demo12.mapper.ProorderMapper;
import com.example.demo12.utils.TokenUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService extends ServiceImpl<ProductMapper,Product> {

    @Resource
    ProductMapper productMapper;

    @Resource
    private MessageMapper messageMapper;

    //redis存取product中的数据
    @Resource
    public RedisTemplate redisTemplate;

    //存数据
    public boolean saveProduct(Product product) {
        boolean b = saveOrUpdate(product);
        if(b){
            System.out.println("product: "+ product.toString());
            redisTemplate.opsForHash().put("product:list",
                    product.getId().toString(), JSONObject.toJSONString(product) );
        }
        return b;
    }

    //根据id在数据库中查数据
    public List<Product> findProduct() {
        Map map = redisTemplate.opsForHash().entries("product:list");
        List<Product> productList = JSONObject.parseArray(map.values().toString(), Product.class);
        try {
            productList = productList.stream().sorted(
                    Comparator.comparing(Product::getProsort,Comparator.nullsLast(Comparator.reverseOrder()))
            ).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    //根据老年用户id，在数据库中查询订单
    public List<ProductVO> findProductByBuyId() {

        Ad ad = TokenUtils.getCurrentAd();
        Proorder proorder = new Proorder();
        proorder.setBuyId(ad.getId());
        List<ProductVO> productList = productMapper.findProductListByBuyId(proorder);

        List<ProductVO> volist = Lists.newArrayList();
        for(Product p : productList) {
            ProductVO vo = new ProductVO();
            BeanUtil.copyProperties(p, vo);

            //所有评论
            List<Message> messages = messageMapper.findAllByForeignId(p.id);
            vo.setRate(BigDecimal.ZERO);
            if(messages.size() > 0) {
                vo.setRate(messages.get(0).getRate());
            }
            volist.add(vo);
        }
        return volist;
    }


}
