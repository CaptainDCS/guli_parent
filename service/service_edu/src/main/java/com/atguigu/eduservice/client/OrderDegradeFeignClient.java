package com.atguigu.eduservice.client;

import com.atguigu.eduservice.feign.OrderFeign;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class OrderDegradeFeignClient implements OrderFeign {
    @Override
    public Boolean isBuyCourse(String courseId, String memberId) {
        throw new GuliException(20001, "系统被撅了，一个一个一个...");
    }
}
