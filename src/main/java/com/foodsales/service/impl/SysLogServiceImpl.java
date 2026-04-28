package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.SysLogMapper;
import com.foodsales.model.SysLog;
import com.foodsales.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired private SysLogMapper sysLogMapper;

    @Override
    public void addLog(Integer userId, String username, String module, String action, String content, String ip) {
        SysLog log = new SysLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setModule(module);
        log.setAction(action);
        log.setContent(content);
        log.setIp(ip);
        log.setCreateTime(LocalDateTime.now());
        sysLogMapper.insert(log);
    }

    @Override
    public PageDTO<SysLog> getLogs(String module, String action, int page, int pageSize) {
        Page<SysLog> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<SysLog> q = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) q.eq(SysLog::getModule, module);
        if (action != null && !action.isEmpty()) q.eq(SysLog::getAction, action);
        q.orderByDesc(SysLog::getCreateTime);
        sysLogMapper.selectPage(p, q);
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }
}