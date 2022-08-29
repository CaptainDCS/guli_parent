package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/eduorder/paylog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @GetMapping("/createNative/{orderNo}")
    private R createNative(@PathVariable("orderNo") String orderNo){

        Map<String, Object> map = payLogService.createNative(orderNo);

        return R.ok().data(map);
    }

    //查询订单状态
    @GetMapping("/queryPayStatus/{orderNo}")
    private R queryPayStatus(@PathVariable("orderNo") String orderNo){

        Map<String, String> map =  payLogService.queryPayStatus(orderNo);
        if(map == null){
            return R.error().message("支付异常");
        }

        if(map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中...");
    }

}

