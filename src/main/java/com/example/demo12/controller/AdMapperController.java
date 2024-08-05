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
import com.example.demo12.controller.dto.UserPasswordDTO;
import com.example.demo12.entity.*;
import com.example.demo12.service.AdService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/ad")

public class AdMapperController {

    @Resource
    private AdService adService;


    @GetMapping
    public List<Ad> show(){
        return adService.list();
    }

    @PostMapping
    private Result save(@RequestBody Ad ad){//新增或者更新
        String adname = ad.getAdname();
        if(StrUtil.isBlank(adname)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        if(ad.getId() != null) {
            ad.setAdpassword(null);
        }else{
            ad.setAdnickname(ad.getAdname());
            if(ad.getAdpassword() == null) {
                ad.setAdpassword("000000");
            }
        }
        return Result.success(adService.saveOrUpdate(ad));
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String adname = userDTO.getAdname();
        String adpassword = userDTO.getAdpassword();
        if(StrUtil.isBlank(adname) || StrUtil.isBlank(adpassword)){
            return Result.error(Constants.CODE_400,"参数错误");
        }

        UserDTO dto = adService.login(userDTO);
        return Result.success(dto);

    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String adname = userDTO.getAdname();
        String adpassword = userDTO.getAdpassword();
        if(StrUtil.isBlank(adname) || StrUtil.isBlank(adpassword)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(adService.register(userDTO));

    }


    @GetMapping("/adname/{adname}")
    public Result findByAdname(@PathVariable String adname){
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("adname",adname);
        Ad one = adService.getOne(queryWrapper);
        return Result.success(one);
    }

    //修改密码
    @PostMapping("/adpassword")
    public Result adpassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        adService.updateAdpassword(userPasswordDTO);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return adService.removeById(id);
    }

    //批量删除接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return adService.removeByIds(ids);
    }

    //分页
    @GetMapping("/page")
    public IPage<Ad> findPage(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize,
                              @RequestParam(defaultValue = "") String adname,
                              @RequestParam(defaultValue = "") String ability,
//                              @RequestParam(defaultValue = "") String oaddress),
                              @RequestParam String adrole){
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Ad> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(adname)){//进行一个判断，如果不为空再进行查询
            queryWrapper.like("adname",adname);
        }
        if(!"".equals(ability)){
            queryWrapper.like("ability",ability);
        }

        queryWrapper.eq("adrole",adrole);
        return adService.page(page,queryWrapper);
    }

    //服务商分页
    @GetMapping("/page2")
    public IPage<Ad> findPage2(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String adname,
                                 @RequestParam(defaultValue = "") String instname,
                                 @RequestParam(defaultValue = "") String nurworkingyears,
                                    @RequestParam String adrole) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Ad> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        if (!"".equals(adname)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("adname", adname);
        }
        if (!"".equals(instname)) {
            queryWrapper.like("instname", instname);
        }
        if (!"".equals(nurworkingyears)) {
            queryWrapper.like("nurworkingyears", nurworkingyears);
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("adrole",adrole);
        return adService.page(page, queryWrapper);

    }

    //机构分页
    @GetMapping("/page3")
    public IPage<Ad> findPage3(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String instname,
                                 @RequestParam(defaultValue = "") String instcertify,
                                 @RequestParam(defaultValue = "") String oaddress,
                               @RequestParam String adrole) {
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<Ad> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        if (!"".equals(instname)) {//进行一个判断，如果不为空再进行查询
            queryWrapper.like("instname", instname);
        }
        if (!"".equals(instcertify)) {
            queryWrapper.like("instcertify", instcertify);
        }
        if (!"".equals(oaddress)) {
            queryWrapper.like("oaddress", oaddress);
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("adrole",adrole);
        return adService.page(page, queryWrapper);

    }

    //导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有的数据
        List<Ad> list = adService.list();

        /*可以通过工具类创建writer写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");*/

        //这里是在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("adname","用户名");
        writer.addHeaderAlias("adnickname","昵称");
        writer.addHeaderAlias("adpassword","密码");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("ability","行为能力");
        writer.addHeaderAlias("adphone","电话");
        writer.addHeaderAlias("oaddress","地址");
        writer.addHeaderAlias("adcreattime","创建时间");
        writer.addHeaderAlias("adavatar","头像");


        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }



    //服务商导出数据
    @GetMapping("/export2")
    public void export2(HttpServletResponse response) throws Exception {
        //从数据库查出所有的数据
        List<Ad> list = adService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("adname", "用户名");
        writer.addHeaderAlias("instname", "公司名称");
        writer.addHeaderAlias("adnickname", "昵称");
        writer.addHeaderAlias("adphone", "电话");
        writer.addHeaderAlias("adpassword", "密码");
        writer.addHeaderAlias("ademail", "邮箱");
        writer.addHeaderAlias("nurcredential", "证书");
        writer.addHeaderAlias("nurworkingyears", "工龄");
        writer.addHeaderAlias("oaddress", "地址");
        writer.addHeaderAlias("adavatar", "头像");
        writer.addHeaderAlias("adcreattime", "创建时间");

        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush (out, true);
        out.close();
        writer.close();
    }

    //服务商导出数据
    @GetMapping("/export3")
    public void export3(HttpServletResponse response) throws Exception {
        //从数据库查出所有的数据
        List<Ad> list = adService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("instname", "公司名称");
        writer.addHeaderAlias("adpassword", "密码");
        writer.addHeaderAlias("instcertify", "凭证");
        writer.addHeaderAlias("ademail", "邮箱");
        writer.addHeaderAlias("adphone", "电话");
        writer.addHeaderAlias("insthistorytime", "成立时间");
        writer.addHeaderAlias("oaddress", "地址");
        writer.addHeaderAlias("instculture", "企业文化");
        writer.addHeaderAlias("adavatar", "头像");
        writer.addHeaderAlias("adcreattime", "创建时间");
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
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        //方式一：通过Javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟Javabean的属性要对应起来( Excel表名:用户信息 )
        List<Ad> list = reader.readAll(Ad.class);
        adService.saveBatch(list);//将导入的数据放到数据库中
        return true;


    }

}
