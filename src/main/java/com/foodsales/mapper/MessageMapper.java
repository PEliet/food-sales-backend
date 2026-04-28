package com.foodsales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foodsales.model.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}