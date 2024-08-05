
package com.example.demo12.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.common.Result;
import com.example.demo12.controller.dto.UserDTO;
import com.example.demo12.entity.User3;
import com.example.demo12.service.UserService3;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user3")

public class UserMapperController3 {


    @Resource
    private UserService3 userService3;

    @GetMapping
    public List<User3> show() {
        return userService3.list();
    }


    @PostMapping //老年人数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody User3 user3) {//新增或者更新
        return userService3.saveUser3(user3);

    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService3.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch3")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService3.removeByIds(ids);
    }

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page3")
    public IPage<User3> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String instname,
                                 @RequestParam(defaultValue = "") String instcertify,
                                 @RequestParam(defaultValue = "") String instaddress) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<User3> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User3> queryWrapper = new QueryWrapper<>();
        if (!"".equals(instname)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("instname", instname);
        }
        if (!"".equals(instcertify)) {
            queryWrapper.like("instcertify", instcertify);
        }
        if (!"".equals(instaddress)) {
            queryWrapper.like("instaddress", instaddress);
        }
        queryWrapper.orderByDesc("id");
        return userService3.page(page, queryWrapper);

    }


    //导出数据
    @GetMapping("/export3")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查出所有的数据
        List<User3> list = userService3.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("instname", "公司名称");
        writer.addHeaderAlias("instpassword", "密码");
        writer.addHeaderAlias("instcertify", "凭证");
        writer.addHeaderAlias("instemail", "邮箱");
        writer.addHeaderAlias("instphone", "电话");
        writer.addHeaderAlias("insthistorytime", "成立时间");
        writer.addHeaderAlias("instaddress", "地址");
        writer.addHeaderAlias("instculture", "企业文化");
        writer.addHeaderAlias("instavatar", "头像");
        writer.addHeaderAlias("instcreattime", "创建时间");

        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入
    @PostMapping("/import3")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User3> list = reader.readAll(User3.class);
        userService3.saveBatch(list);
        return true;
    }
}


