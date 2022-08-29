package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {

    boolean sendMsm(Map<String, Object> map, String phone);
}
