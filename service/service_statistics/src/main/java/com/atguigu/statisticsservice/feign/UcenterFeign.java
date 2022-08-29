package com.atguigu.statisticsservice.feign;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter")
public interface UcenterFeign {

    //查询某一天的注册人数
    @GetMapping("/eduucenter/member/countRegister/{date}")
    public R countRegister(@PathVariable("date") String date);

}
