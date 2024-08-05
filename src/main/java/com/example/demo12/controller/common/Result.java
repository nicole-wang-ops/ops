package com.example.demo12.controller.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//接口统一返回包装类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //状态码，表示接口调用的执行情况；
    private String code;
    //提示信息，对接口调用结果进行描述；
    private String msg;
    //泛型数据，表示接口调用返回的数据。
    private Object data;


    public static Result success(){ return new Result(Constants.CODE_200,"",null); }
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);

    }
}
