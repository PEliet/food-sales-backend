package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.MessageMapper;
import com.foodsales.model.Message;
import com.foodsales.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired private MessageMapper messageMapper;

    @Override
    public List<Message> getUserMessages(Integer userId, Integer type) {
        LambdaQueryWrapper<Message> q = new LambdaQueryWrapper<Message>().eq(Message::getUserId, userId);
        if (type != null && type > 0) q.eq(Message::getType, type);
        q.orderByDesc(Message::getCreateTime);
        return messageMapper.selectList(q);
    }

    @Override
    public void sendMessage(Integer userId, String title, String content, Integer type, Integer relatedId) {
        Message m = new Message();
        m.setUserId(userId);
        m.setTitle(title);
        m.setContent(content);
        m.setType(type);
        m.setIsRead(0);
        m.setRelatedId(relatedId);
        m.setCreateTime(LocalDateTime.now());
        messageMapper.insert(m);
    }

    @Override
    public void markAsRead(Integer messageId) {
        Message m = messageMapper.selectById(messageId);
        if (m != null) { m.setIsRead(1); messageMapper.updateById(m); }
    }

    @Override
    public int getUnreadCount(Integer userId) {
        return messageMapper.selectCount(new LambdaQueryWrapper<Message>().eq(Message::getUserId, userId).eq(Message::getIsRead, 0)).intValue();
    }

    @Override
    public PageDTO<Message> getAllMessages(int page, int pageSize) {
        Page<Message> p = new Page<>(page, pageSize);
        messageMapper.selectPage(p, new LambdaQueryWrapper<Message>().orderByDesc(Message::getCreateTime));
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }
}