
package com.example.demo12.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.Role;
import com.example.demo12.service.RoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/role")

public class RoleMapperController {


    @Resource
    private RoleService roleService;

    @GetMapping
    public List<Role> show() {
        return roleService.list();
    }


    @PostMapping //数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody Role role) {//新增或者更新
        return roleService.saveRole(role);

    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return roleService.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return roleService.removeByIds(ids);
    }

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page")
    public IPage<Role> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String rolename,
                                 @RequestParam(defaultValue = "") String roledescription) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (!"".equals(rolename)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("rolename", rolename);
        }
        if (!"".equals(roledescription)) {
            queryWrapper.like("roledescription", roledescription);
        }
        queryWrapper.orderByDesc("id");
        return roleService.page(page, queryWrapper);

    }

    //绑定角色和菜单关系
    //roleid   角色Id
    //menuids  菜单id数组
    @PostMapping("/roleMenu/{roleid}")
    public Result roleMenu(@PathVariable Integer roleid, @RequestBody List<Integer> menuids) {
        roleService.setRoleMenu(roleid,menuids);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleid}")
    public Result getRoleMenu(@PathVariable Integer roleid) {
        return Result.success(roleService.getRoleMenu(roleid));
    }
}


