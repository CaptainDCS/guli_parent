package com.atguigu.statisticsservice.service;

import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author captainM
 * @since 2022-08-27
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void getRegisterCount(String date);

    Map<String, Object> getShowData(String type, String begin, String end);
}
