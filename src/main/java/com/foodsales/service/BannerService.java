package com.foodsales.service;

import com.foodsales.dto.*;
import com.foodsales.model.*;
import com.foodsales.dto.PageDTO;
import java.util.List;
import java.util.Map;

public interface BannerService {
    java.util.List<Banner> getActiveBanners();
    PageDTO<Banner> getAllBanners(int page, int pageSize);
    Banner addBanner(Banner banner);
    Banner updateBanner(Banner banner);
    void deleteBanner(Integer id);
}