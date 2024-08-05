package com.example.demo12.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.*;
import com.example.demo12.service.NurproorderService;
import com.example.demo12.service.ProductService;
import com.example.demo12.service.ProorderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/product")

public class ProductMapperController {


    @Resource
    private ProductService productService;

    @Resource
    public ProorderService proorderService;

    @Resource
    public NurproorderService nurproorderService;



    @GetMapping//商品数据查询路径   【@GetMapping("/show")】
    //@CrossOrigin(origins = "*")
    public List<Product> show(){
        return productService.list();
    }

    //经过Redis查询商品数据
    @GetMapping("/findProductList")
    public Result findProductList() {
        return Result.success(productService.findProduct());
    }


    @PostMapping   //老年人数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody Product product){//新增或者更新
        return productService.saveProduct(product);

    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return productService.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return productService.removeByIds(ids);
    }

    @GetMapping("/proname/{proname}")
    public Result findByProname(@PathVariable String proname){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("proname",proname);
        Product one = productService.getOne(queryWrapper);
        return Result.success(one);
    }

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page")
    public IPage<Product> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String proname,
                                @RequestParam(defaultValue = "") String proassname,
                                @RequestParam(defaultValue = "") String proinsname){
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Product> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(proname)){//进行一个判断，如果不为空再进行查询
            queryWrapper.like("proname",proname);
        }
        if(!"".equals(proassname)){
            queryWrapper.like("proassname",proassname);
        }
        if(!"".equals(proinsname)){
            queryWrapper.like("proinsname",proinsname);//idea 直接帮忙加上了and
            //queryWrapper.and(w -> w.like("o_address",o_address));
        }


        /*//获取当前用户信息(关于token的，不能用，因为这里是User，而定义的时候用的是登录用户Ad，但是暂时不知道怎么改！！！！！！！)
        Ad currentAd = TokenUtils.getCurrentAd();
        System.out.println("获取当前用户信息================================="+currentAd.getAdnickname());*/

        return productService.page(page,queryWrapper);

    }

    //根据ID显示商品（加入订单）(老年用户)
    @GetMapping("/proname/findByBuyId")
    public Result findProductByBuyId(){
        List<ProductVO> products = productService.findProductByBuyId();
        return Result.success(products);
    }

    //加入订单(老年用户)
    @PostMapping("/proname/buyPro")
    public boolean buyPro(@RequestBody JSONObject jo) {
        proorderService.saveProOrder(jo.getInteger("id"));
        return true;
    }

    //兑换(老年用户)
    @PostMapping("/proname/changePro")
    public boolean changePro(@RequestBody JSONObject jo) {
        proorderService.saveProOrderchange(jo.getInteger("id"));
        return true;
    }

    @PostMapping("/proname/saveBussiness")
    private boolean saveBusinessRate(@RequestBody Proorder dto){
        try {
            proorderService.saveBussiness(dto);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
