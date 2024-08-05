package com.example.demo12.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.entity.Message;

import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageMapper extends BaseMapper<Message> {

    List<Message> findAllByForeignId(Integer foreignId);

}
//public interface MessageMapper extends JpaRepository<Message,Integer> {
//}
