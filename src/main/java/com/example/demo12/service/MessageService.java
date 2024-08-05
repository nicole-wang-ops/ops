package com.example.demo12.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.*;
import com.example.demo12.mapper.MessageMapper;
import com.example.demo12.mapper.ProductMapper;
import com.example.demo12.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;


@Service
public class MessageService extends ServiceImpl<MessageMapper,Message> {
    @Resource
    MessageMapper messageMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    AdService adService;

    public boolean saveMessage(Message message) {


        return saveOrUpdate(message);
    }

    //评论获得商品ID
    public void saveProMessage(Integer foreignId) {
        Message message = new Message();
        message.setForeignId(foreignId);

        Ad ad = TokenUtils.getCurrentAd();
        message.setAdid(ad.getId());

        Product product = productMapper.selectById(foreignId);
        //adService.creditUpdate(ad);

        messageMapper.insert(message);
    }

    //评论积分+5
    public boolean saveMessage1(Message message) {

         //* 新增文章，积分 + 5

        Ad ad = TokenUtils.getCurrentAd();
        ad.setCredit(ad.getCredit() + 1);
        adService.creditUpdate(ad);

        return saveOrUpdate(message);
    }
}
