package com.atguigu.statisticsservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.statisticsservice.feign.UcenterFeign;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/statisticsservice/statisticsdaily")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计某一天的注册人数,生成统计数据
    @PostMapping("/registerCount/{date}")
    @Transactional
    public R registerCount(@PathVariable("date") String date){
        statisticsDailyService.getRegisterCount(date);
        return R.ok();
    }

    //显示图标数据，主要获取日期和相关数量两部分
    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable("type") String type, @PathVariable("begin")String begin, @PathVariable("end") String end){

        Map<String, Object> map = statisticsDailyService.getShowData(type, begin, end);

        return R.ok().data(map);
    }


}
