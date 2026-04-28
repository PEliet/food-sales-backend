package com.foodsales.controller;

import com.foodsales.model.Logistics;
import com.foodsales.model.LogisticsTrack;
import com.foodsales.service.LogisticsService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/logistics")
public class LogisticsController {

    @Autowired private LogisticsService logisticsService;

    @GetMapping("/{orderId}")
    public ResponseResult<Map<String,Object>> getLogistics(@PathVariable Integer orderId) {
        Logistics l = logisticsService.getLogisticsByOrderId(orderId);
        if (l == null) return ResponseResult.error("暂无物流信息");
        List<LogisticsTrack> tracks = logisticsService.getTracks(l.getLogisticsId());
        Map<String,Object> result = new HashMap<>();
        result.put("logistics", l);
        result.put("tracks", tracks);
        return ResponseResult.success(result);
    }

    @GetMapping("/list")
    public ResponseResult<List<Logistics>> list() {
        return ResponseResult.success(null);
    }

    @PostMapping("/create")
    public ResponseResult<Logistics> create(@RequestParam Integer orderId,
            @RequestParam String company, @RequestParam String trackingNo) {
        return ResponseResult.success(logisticsService.createLogistics(orderId, company, trackingNo));
    }

    @PostMapping("/track")
    public ResponseResult<Void> addTrack(@RequestParam Integer logisticsId,
            @RequestParam String operation, @RequestParam(required=false) String operator,
            @RequestParam(required=false) String location) {
        logisticsService.updateTrack(logisticsId, operation, operator, location);
        return ResponseResult.success(null);
    }
}