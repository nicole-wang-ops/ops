
package com.example.demo12.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.Dict;
import com.example.demo12.entity.Menu;
import com.example.demo12.mapper.DictMapper;
import com.example.demo12.service.MenuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/menu")

public class MenuMapperController {


    @Resource
    private MenuService menuService;

    @Resource
    private DictMapper dictMapper;

    @GetMapping("/show")
    public List<Menu> show() {
        return menuService.list();
    }


    @PostMapping //数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody Menu menu) {//新增或者更新
        return menuService.saveRole(menu);

    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return menuService.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return menuService.removeByIds(ids);
    }

    @GetMapping("/findAll")
    public Result findAll(@RequestParam(defaultValue = "") String menuname){


        return Result.success(menuService.findMenus(menuname));
    };

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page")
    public IPage<Menu> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String menuname,
                                 @RequestParam(defaultValue = "") String menudescription) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (!"".equals(menuname)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("menuname", menuname);
        }
        if (!"".equals(menudescription)) {
            queryWrapper.like("menudescription", menudescription);
        }
        queryWrapper.orderByDesc("id");
        return menuService.page(page, queryWrapper);

    }

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dicttype", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }

}


