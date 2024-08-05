package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName(value ="news")
@ToString
public class News implements Serializable {
    //private static final serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String nurname;

    private String adname;

    private String descr;

    private String content;

    private String cover;

    private String time;

    private String statue;

    private Integer count;

    private String category;
/*
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescr(){
        return descr;
    }

    public void setDescr(String descr){
        this.descr = descr;
    }


    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getCover(){
        return cover;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public Integer getCount(){
        return count;
    }

    public void setCount(Integer count){
        this.count = count;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }*/

}
