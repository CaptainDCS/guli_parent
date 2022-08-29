package com.atguigu.ucenterservice.mapper;

import com.atguigu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author captainM
 * @since 2022-08-22
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer getCountRegisterDay(String date);
}
