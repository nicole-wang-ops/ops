package com.example.demo12.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.Nurproorder;
import com.example.demo12.entity.Product;
import com.example.demo12.entity.Proorder;
import com.example.demo12.mapper.NurproorderMapper;
import com.example.demo12.mapper.ProductMapper;
import com.example.demo12.mapper.ProorderMapper;
import com.example.demo12.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NurproorderService extends ServiceImpl<NurproorderMapper, Nurproorder> {

    @Resource
    NurproorderMapper nurproorderMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    AdService adService;

    public void saveNurproOrder(Integer pid) {
        Nurproorder nurproorder = new Nurproorder();
        nurproorder.setPid(pid);

        Ad ad = TokenUtils.getCurrentAd();
        nurproorder.setBuyId(ad.getId());

        Product product = productMapper.selectById(pid);
        ad.setCredit(ad.getCredit() + product.getProprice());
        adService.creditUpdate(ad);

        nurproorderMapper.insert(nurproorder);
    }

    public void saveNurproOrderchange(Integer pid) {
        Nurproorder nurproorder = new Nurproorder();
        nurproorder.setPid(pid);

        Ad ad = TokenUtils.getCurrentAd();
        nurproorder.setBuyId(ad.getId());

        Product product = productMapper.selectById(pid);
        ad.setCredit(ad.getCredit() - product.getProprice());
        adService.creditUpdate(ad);

        nurproorderMapper.insert(nurproorder);
    }




}
