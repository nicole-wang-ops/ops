package com.example.demo12.controller;

import cn.hutool.core.collection.CollUtil;
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
import com.example.demo12.entity.Ad;
import com.example.demo12.mapper.UserMapper;
import com.example.demo12.entity.User;
import com.example.demo12.mapper.UserMapper;
import com.example.demo12.service.UserService;
import com.example.demo12.utils.TokenUtils;
import com.sun.org.apache.xpath.internal.objects.XNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/user")

public class UserMapperController {


    @Resource
    private UserService userService;


    @GetMapping//老年人数据查询路径   【@GetMapping("/show")】
    //@CrossOrigin(origins = "*")
    public List<User> show(){
        return userService.list();
    }


    @PostMapping   //老年人数据添加、修改路径  【原本为@PostMapping("/save"),为了使前端添加数据】
    private boolean save(@RequestBody User user){//新增或者更新
        return userService.saveUser(user);

    }




    /*@PostMapping
    private Result save(@RequestBody User user){//新增或者更新
        String name = user.getName();
        if(StrUtil.isBlank(name)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        if(user.getId() != null) {
            user.setOpassword(null);
        }else{
            user.setNickname(user.getName());
            if(user.getOpassword() == null) {
                user.setOpassword("000000");
            }
        }
        return Result.success(userService.saveOrUpdate(user));
    }


    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String name = userDTO.getName();
        String opassword = userDTO.getOpassword();
        if(StrUtil.isBlank(name) || StrUtil.isBlank(opassword)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);

        return Result.success(dto);

    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String adname = userDTO.getAdname();
        String adpassword = userDTO.getAdpassword();
        if(StrUtil.isBlank(adname) || StrUtil.isBlank(adpassword)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.register(userDTO));

    }


    @GetMapping("/name/{name}")
    public Result findByName(@PathVariable String name){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        User one = userService.getOne(queryWrapper);
        return Result.success(one);
    }

    //修改密码
    @PostMapping("/opassword")
    public Result opassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updateOpassword(userPasswordDTO);
        return Result.success();
    }*/



    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    /*批量删除接口*/
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }



    //@RequestParam 接受 ？pageNum=1&pageSize=10 前端如果有这个，可以映射到后端这里
    /*@GetMapping("/page")//分页查询路径接口:localhost:9090/user/page?pageNum=1&pageSize=10
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String name){
        pageNum=(pageNum-1)*pageSize;
        name = "%" + name + "%";
        List<User> date = userMapper.selectPage(pageNum,pageSize,name);
        Integer total = userMapper.selectTotal(name);
        Map<String,Object> res = new HashMap<>();
        res.put("date",date);
        res.put("total",total);
        return res;
    }*/

    //分页查询 - mybatis-plus的方式(实现多条件查询比较简单)
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String ability,
                                @RequestParam(defaultValue = "") String oaddress){
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(name)){//进行一个判断，如果不为空再进行查询
            queryWrapper.like("name",name);
        }
        if(!"".equals(ability)){
            queryWrapper.like("ability",ability);
        }
        if(!"".equals(oaddress)){
            queryWrapper.like("oaddress",oaddress);//idea 直接帮忙加上了and
            //queryWrapper.and(w -> w.like("o_address",o_address));
        }


        /*//获取当前用户信息(关于token的，不能用，因为这里是User，而定义的时候用的是登录用户Ad，但是暂时不知道怎么改！！！！！！！)
        Ad currentAd = TokenUtils.getCurrentAd();
        System.out.println("获取当前用户信息================================="+currentAd.getAdnickname());*/

        return userService.page(page,queryWrapper);

    }


    //导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查出所有的数据
        List<User> list = userService.list();

        /*可以通过工具类创建writer 写出到磁盘路径
        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");*/

        //这里是在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义别名
        writer.addHeaderAlias("name","用户名");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("opassword","密码");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("ability","行为能力");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("oaddress","地址");
        writer.addHeaderAlias("creattime","创建时间");
        writer.addHeaderAlias("avatar","头像");

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

        //导出的逻辑：首先从数据库获取到数据，然后新建一个writer对象，往writer对象里面写数据，把写完数据的writer对象刷新到输出流里面去，最后通过输出流给它返回到浏览器。

    }

    //导入
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        //方式一：通过Javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟Javabean的属性要对应起来( Excel表名:用户信息 )
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);//将导入的数据放到数据库中
        return true;

        /*//方式二：忽略表头的中文，直接读取表的内容(Excel表名:用户信息(7)  )
        //缺点：row.get()这里每一列都是固定的。
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setName(row.get(0).toString());
            user.setOPassword(row.get(1).toString());
            user.setAge(Integer.parseInt(row.get(2).toString()));
            user.setAbility(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setOAddress(row.get(5).toString());
            user.setAvatar(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return true;
*/

        /*写了一个MUltipartFile file 对象，前端上传文件之后，转换成这样一个文件对象，
         我们从这个文件对象中获取inputSream，
         获取后再new ExcelUtil的时候将它设置进去，这是ExcelReader这个语句提供的方法（https://hutool.cn/docs官网中也有)。
         官网中设的是地址，只是这里直接将inputStream这个流塞到reader中去了
         塞完之后就可以直接读取了，readAll（）中指定了一个范型“User.class”，如果不指定可能会输出object类型。
         */
    }


}
