package com.example.demo12.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.entity.Proorder;


public interface ProorderMapper extends BaseMapper<Proorder> {


    void updateByPid(Proorder dto);

}
