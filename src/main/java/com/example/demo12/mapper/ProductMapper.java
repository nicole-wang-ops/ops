package com.example.demo12.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.entity.Product;
import com.example.demo12.entity.ProductVO;
import com.example.demo12.entity.Proorder;

import java.util.List;


public interface ProductMapper extends BaseMapper<Product> {
    /*@Update("update the_aged set opassword = #{newOpassword} where name = #{name} and opassword = #{opassword}")
    int updateOpassword(UserPasswordDTO userPasswordDTO);*/

    List<ProductVO> findProductListByBuyId(Proorder proorder);
}
