package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.eduservice.feign.UcenterFeign;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author captainM
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterFeign ucenterFeign;

    //分页查询所有评论
    @GetMapping("/getCommentListByPage/{courseId}/{page}/{limit}")
    public R getCommentListByPage(@PathVariable("courseId") String courseId,@PathVariable("page") long page, @PathVariable("limit") long limit){

        //分页
        Page<EduComment> commentPage = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId).orderByDesc("gmt_create");//根据课程ID查询对应课程下的评论，并根据创建时间降序排列
        commentService.page(commentPage, wrapper);

        //获取分页后的所有信息
        List<EduComment> records = commentPage.getRecords();
        long current = commentPage.getCurrent();
        long pages = commentPage.getPages();
        long size = commentPage.getSize();
        long total = commentPage.getTotal();
        boolean hasNext = commentPage.hasNext();
        boolean hasPrevious = commentPage.hasPrevious();
        //将分页信息和数据封装在map集合中返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        map.put("records", records);

        return R.ok().data(map);
    }

    //添加评论
    @PostMapping("/addComment")
    public R addComment(HttpServletRequest request, @RequestBody(required = false) EduComment eduComment){
        //获取前端传回的Token字符串并解析用户ID
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            //若返回的用户ID为空则提示登录
            throw new GuliException(20001, "请先登录，再评论！");
        }
        //将用户ID封装到对象中
        eduComment.setMemberId(memberId);
        //远程调用获取用户信息
        UcenterMemberVo member = ucenterFeign.getMemberInfoById(memberId);
        eduComment.setNickname(member.getNickname());
        eduComment.setAvatar(member.getAvatar());

        //将评论添加到数据库
        commentService.save(eduComment);

        return R.ok();
    }

}

