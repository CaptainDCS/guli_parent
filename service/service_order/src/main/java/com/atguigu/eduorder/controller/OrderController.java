package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //生成订单
    @PostMapping("/createOrder/{courseId}")
    public R  createOrder(@PathVariable("courseId") String courseId, HttpServletRequest request){
        //获取请求头中的用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录");
        }
        String orderId = orderService.createOrders(courseId, memberId);
        return R.ok().data("orderId", orderId);
    }

    //根据订单ID查询订单信息
    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable("orderId") String orderId){

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderId);
        Order order = orderService.getOne(orderQueryWrapper);

        return R.ok().data("order", order);
    }

    //根据课程ID和用户ID查询订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public Boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId).eq("member_id", memberId).eq("status", 1);
        //获取是否有已支付的订单
        int count = orderService.count(wrapper);
        if(count > 0){//已支付
            return true;
        }
        return false;
    }

}

