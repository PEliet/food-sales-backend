package com.foodsales.controller;

import com.foodsales.dto.PageDTO;
import com.foodsales.model.Message;
import com.foodsales.service.MessageService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired private MessageService messageService;

    @GetMapping("/list")
    public ResponseResult<PageDTO<Message>> list(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(messageService.getAllMessages(page, pageSize));
    }

    @PostMapping("/send")
    public ResponseResult<Void> send(@RequestParam Integer userId, @RequestParam String title,
            @RequestParam String content, @RequestParam(defaultValue="0") Integer type,
            @RequestParam(required=false) Integer relatedId) {
        messageService.sendMessage(userId, title, content, type, relatedId);
        return ResponseResult.success(null);
    }
}