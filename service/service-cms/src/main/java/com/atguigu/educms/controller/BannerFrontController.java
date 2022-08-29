package com.atguigu.educms.controller;

import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
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
@RequestMapping("/educms/bannerFront")
//@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("/getAll")
    public R getAll(){
        List<CrmBanner> bannerList = bannerService.getAllBanner();
        return R.ok().data("list", bannerList);
    }
}
