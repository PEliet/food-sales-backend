package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface MessageService {
    java.util.List<Message> getUserMessages(Integer userId, Integer type);
    void sendMessage(Integer userId, String title, String content, Integer type, Integer relatedId);
    void markAsRead(Integer messageId);
    int getUnreadCount(Integer userId);
    PageDTO<Message> getAllMessages(int page, int pageSize);
}