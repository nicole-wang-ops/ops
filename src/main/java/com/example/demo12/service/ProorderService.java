package com.example.demo12.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.News;
import com.example.demo12.entity.Product;
import com.example.demo12.entity.Proorder;
import com.example.demo12.mapper.ProductMapper;
import com.example.demo12.mapper.ProorderMapper;
import com.example.demo12.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProorderService extends ServiceImpl<ProorderMapper, Proorder> {

    @Resource
    ProorderMapper proorderMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    AdService adService;

    public void saveProOrder(Integer pid) {
        Proorder proorder = new Proorder();
        proorder.setPid(pid);

        Ad ad = TokenUtils.getCurrentAd();
        proorder.setBuyId(ad.getId());

        Product product = productMapper.selectById(pid);
        ad.setCredit(ad.getCredit() + product.getProprice());
        adService.creditUpdate(ad);

        proorderMapper.insert(proorder);
    }

    public void saveBussiness(Proorder dto) {

        proorderMapper.updateByPid(dto);
        // 增加积分
        Ad ad = TokenUtils.getCurrentAd();
        ad.setCredit(ad.getCredit() + 1);
        adService.creditUpdate(ad);

    }

    public void saveProOrderchange(Integer pid) {
        Proorder proorder = new Proorder();
        proorder.setPid(pid);

        Ad ad = TokenUtils.getCurrentAd();
        proorder.setBuyId(ad.getId());

        Product product = productMapper.selectById(pid);
        ad.setCredit(ad.getCredit() - 10);
//        ad.setCredit(ad.getCredit() - product.getProprice());
        adService.creditUpdate(ad);

        proorderMapper.insert(proorder);
    }


    //积分兑换
    /*public void saveProOrderchange1(Integer pid) {
        Proorder proorder = new Proorder();
        proorder.setPid(pid);

        Ad ad = TokenUtils.getCurrentAd();
        proorder.setBuyId(ad.getId());

//        Product product = productMapper.selectById(pid);
        ad.setCredit(ad.getCredit() - 10);
        adService.creditUpdate(ad);

        proorderMapper.insert(proorder);
    }*/



}
