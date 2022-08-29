package com.atguigu.statisticsservice.feign.client;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.statisticsservice.feign.UcenterFeign;
import org.springframework.stereotype.Component;

@Component
public class UnenterClient implements UcenterFeign {
    @Override
    public R countRegister(String date) {
        throw new GuliException(20001, "服务器有十分甚至九分的像是被撅了...");
    }
}
