package com.foodsales.controller;

import com.foodsales.dto.PageDTO;
import com.foodsales.model.Banner;
import com.foodsales.service.BannerService;
import com.foodsales.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired private BannerService bannerService;

    @GetMapping("/list")
    public ResponseResult<List<Banner>> list() {
        return ResponseResult.success(bannerService.getActiveBanners());
    }

    @GetMapping("/admin/list")
    public ResponseResult<PageDTO<Banner>> adminList(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="20") int pageSize) {
        return ResponseResult.success(bannerService.getAllBanners(page, pageSize));
    }

    @PostMapping("/add")
    public ResponseResult<Banner> add(@RequestBody Banner banner) {
        return ResponseResult.success(bannerService.addBanner(banner));
    }

    @PutMapping("/update")
    public ResponseResult<Banner> update(@RequestBody Banner banner) {
        return ResponseResult.success(bannerService.updateBanner(banner));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Integer id) {
        bannerService.deleteBanner(id);
        return ResponseResult.success(null);
    }
}