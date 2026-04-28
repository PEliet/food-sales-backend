package com.foodsales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.foodsales.dto.PageDTO;
import com.foodsales.mapper.BannerMapper;
import com.foodsales.model.Banner;
import com.foodsales.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired private BannerMapper bannerMapper;

    @Override
    public List<Banner> getActiveBanners() {
        return bannerMapper.selectList(new LambdaQueryWrapper<Banner>().eq(Banner::getStatus, 1).orderByAsc(Banner::getSort));
    }

    @Override
    public PageDTO<Banner> getAllBanners(int page, int pageSize) {
        Page<Banner> p = new Page<>(page, pageSize);
        bannerMapper.selectPage(p, new LambdaQueryWrapper<Banner>().orderByAsc(Banner::getSort));
        return PageDTO.of(p.getTotal(), page, pageSize, p.getRecords());
    }

    @Override
    public Banner addBanner(Banner banner) {
        banner.setCreateTime(LocalDateTime.now());
        bannerMapper.insert(banner);
        return banner;
    }

    @Override
    public Banner updateBanner(Banner banner) {
        bannerMapper.updateById(banner);
        return banner;
    }

    @Override
    public void deleteBanner(Integer id) { bannerMapper.deleteById(id); }
}