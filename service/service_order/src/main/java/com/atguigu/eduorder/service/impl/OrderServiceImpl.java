package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.vo.CourseVo;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.feign.CourseFeign;
import com.atguigu.eduorder.feign.UcenterFegin;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseFeign courseFeign;
    @Autowired
    private UcenterFegin ucenterFegin;

    //生成订单获取订单ID
    @Override
    public String createOrders(String courseId, String memberId) {
        //远程调用Ucenter模块根据用户ID获取用户信息

        UcenterMemberVo memberVo = ucenterFegin.getMemberInfoById(memberId);
        //远程调用EduService模块根据课程ID获取课程信息
        CourseVo courseVo = courseFeign.getCourseInfoById(courseId);

        //创建Order对象存放需要写入数据库中的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseVo.getTitle());
        order.setCourseCover(courseVo.getCover());
        order.setTeacherName(courseVo.getTeacherName());
        order.setTotalFee(courseVo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberVo.getMobile());
        order.setNickname(memberVo.getNickname());

        order.setStatus(0);//支付状态，0表示未支付1表示已支付
        order.setPayType(1);//支付类型，1表示微信支付，2表示支付宝支付
        //写入数据库
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
