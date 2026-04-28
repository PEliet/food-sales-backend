package com.foodsales.controller;

import com.foodsales.service.DashboardService;
import com.foodsales.util.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired private DashboardService dashboardService;

    @GetMapping("/download")
    public void download(@RequestParam String type,
                         @RequestParam String startDate,
                         @RequestParam String endDate,
                         HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(type + "_" + startDate + "_" + endDate + ".xlsx", "UTF-8"));

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Report");
            Row header = sheet.createRow(0);
            String[] cols;
            List<Map<String, Object>> data;

            if ("order".equals(type)) {
                cols = new String[]{"日期", "订单数", "销售额"};
                data = dashboardService.getSalesTrend("month");
            } else if ("product".equals(type)) {
                cols = new String[]{"商品名", "销量", "销售额"};
                data = dashboardService.getTopProducts(100);
            } else {
                cols = new String[]{"指标", "数值"};
                Map<String, Object> summary = dashboardService.getSalesSummary(startDate, endDate);
                data = List.of(
                        Map.of("name", "总订单数", "value", summary.get("totalOrders")),
                        Map.of("name", "总收入", "value", summary.get("totalRevenue")),
                        Map.of("name", "平均客单价", "value", summary.get("avgOrderValue"))
                );
            }

            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            int rowNum = 1;
            for (Map<String, Object> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (Object val : rowData.values()) {
                    if (val instanceof Number) row.createCell(cellNum).setCellValue(((Number) val).doubleValue());
                    else row.createCell(cellNum).setCellValue(val != null ? val.toString() : "");
                    cellNum++;
                }
            }

            try (OutputStream os = response.getOutputStream()) {
                wb.write(os);
            }
        }
    }
}