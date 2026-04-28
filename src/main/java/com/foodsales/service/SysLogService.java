package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface SysLogService {
    void addLog(Integer userId, String username, String module, String action, String content, String ip);
    PageDTO<SysLog> getLogs(String module, String action, int page, int pageSize);
}