package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台管理轮播图Banner
 * </p>
 *
 * @author captainM
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/educms/bannerAdmin")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable("page") long page, @PathVariable("limit") long limit){

        Page<CrmBanner> bannerPage = new Page<>(page, limit);
        bannerService.page(bannerPage, null);

        long total = bannerPage.getTotal();
        List<CrmBanner> records = bannerPage.getRecords();
        return R.ok().data("total", total).data("records", records);
    }

    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    @GetMapping("/getBannerByiId/{id}")
    public R getBannerByiId(@PathVariable("id") String id){
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @PostMapping("/updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        bannerService.updateById(crmBanner);
        return R.ok();
    }

    @DeleteMapping("/deleteBanner/{id}")
    public R deleteBanner(@PathVariable("id") String id){
        bannerService.removeById(id);
        return R.ok();
    }

}

