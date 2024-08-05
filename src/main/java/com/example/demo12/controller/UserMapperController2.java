
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
import com.example.demo12.entity.User2;
import com.example.demo12.service.UserService2;
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
@RequestMapping("/user2")

public class UserMapperController2 {


    @Resource
    private UserService2 userService2;


    @GetMapping
    public List<User2> show() {
        return userService2.list();
    }


    @PostMapping  //老年人数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody User2 user2) {//新增或者更新
        return userService2.saveUser2(user2);

    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService2.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch2")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService2.removeByIds(ids);
    }

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page2")
    public IPage<User2> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String nurname,
                                @RequestParam(defaultValue = "") String instname,
                                @RequestParam(defaultValue = "") String nurworkingyears) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<User2> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User2> queryWrapper = new QueryWrapper<>();
        if (!"".equals(nurname)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("nurname", nurname);
        }
        if (!"".equals(instname)) {
            queryWrapper.like("instname", instname);
        }
        if (!"".equals(nurworkingyears)) {
            queryWrapper.like("nurworkingyears", nurworkingyears);
        }
        queryWrapper.orderByDesc("id");
        return userService2.page(page, queryWrapper);

    }


    //导出数据
    @GetMapping("/export2")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查出所有的数据
        List<User2> list = userService2.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("nurname", "用户名");
        writer.addHeaderAlias("instname", "公司名称");
        writer.addHeaderAlias("nurnickname", "昵称");
        writer.addHeaderAlias("nurphone", "电话");
        writer.addHeaderAlias("nurpassword", "密码");
        writer.addHeaderAlias("nuremail", "邮箱");
        writer.addHeaderAlias("nurcredential", "证书");
        writer.addHeaderAlias("nurworkingyears", "工龄");
        writer.addHeaderAlias("nuraddress", "地址");
        writer.addHeaderAlias("nuravatar", "头像");
        writer.addHeaderAlias("nurcreattime", "创建时间");

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
    @PostMapping("/import2")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User2> list = reader.readAll(User2.class);
        userService2.saveBatch(list);
        return true;
    }
}


