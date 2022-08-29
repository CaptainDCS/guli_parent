package com.atguigu.eduservice.client;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.eduservice.feign.UcenterFeign;
import com.atguigu.eduservice.feign.VodFeign;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class UcenterDegradeFeignClient implements UcenterFeign {
    @Override
    public UcenterMemberVo getMemberInfoById(String memberId) {
        throw new GuliException(20001, "服务器被透了，请稍后...");
    }
}
