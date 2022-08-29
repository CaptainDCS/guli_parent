package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-22
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember ucenterMember) {
        //获取手机号和密码
        String mobile = ucenterMember.getMobile();
        String pwd = ucenterMember.getPassword();

        //对获取到的手机号和密码做非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(pwd)){
            throw new GuliException(20001, "登录失败！");
        }

        //判断手机号是否存在
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if(StringUtils.isEmpty(member)){
            throw new GuliException(20001, "手机号不存在！");
        }

        //判断密码是否正确
        if(!MD5.encrypt(pwd).equals(member.getPassword())){
            throw new GuliException(20001, "密码错误！");
        }

        //判断用户是否被禁用
        if(member.getIsDisabled()){
            throw new GuliException(20001, "账号封禁中！");
        }

        //登录成功，生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();//获取验证码
        String mobile = registerVo.getMobile();//获取手机号
        String nickname = registerVo.getNickname();//获取昵称
        String password = registerVo.getPassword();//获取密码

        //判断表单提交是否有空值
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)|| StringUtils.isEmpty(nickname)){
            throw new GuliException(20001, "注册失败！");
        }
        //判断验证码是否正确
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!redisCode.equals(code)){
            throw new GuliException(20001, "验证码错误！");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(20001, "用户已存在！");
        }

        //将注册信息提交数据
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://online-teach-file.oss-cn-beijing.aliyuncs.com/teacher/2019/10/30/de47ee9b-7fec-43c5-8173-13c5f7f689b2.png");

        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer getCountRegisterDay(String date) {
        return baseMapper.getCountRegisterDay(date);
    }
}
