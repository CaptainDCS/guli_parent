package com.atguigu.eduorder.feign.client;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.eduorder.feign.UcenterFegin;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class UcenterClient implements UcenterFegin {
    @Override
    public UcenterMemberVo getMemberInfoById(String memberId) {
        throw new GuliException(20001, "系统被透了，无法获取到用户信息，请稍后...");
    }
}
