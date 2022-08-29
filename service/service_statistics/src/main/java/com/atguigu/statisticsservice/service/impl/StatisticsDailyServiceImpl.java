package com.atguigu.statisticsservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.atguigu.statisticsservice.feign.UcenterFeign;
import com.atguigu.statisticsservice.mapper.StatisticsDailyMapper;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author captainM
 * @since 2022-08-27
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterFeign ucenterFeign;

    @Override
    public void getRegisterCount(String date) {

        //添加数据之前先删除对应日期的记录
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", date);
        baseMapper.delete(wrapper);

        R registerResult = ucenterFeign.countRegister(date);
        Integer countRegister = (Integer) registerResult.getData().get("countRegister");

        //将获取到的统计数据写入数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(countRegister);//注册人数
        statisticsDaily.setDateCalculated(date);//统计的日期
        /**
         * 以下统计数据暂时使用随机数代替 TODO
         */
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100, 300));//视频观看人数
        statisticsDaily.setLoginNum(RandomUtils.nextInt(10, 28));//登录人数
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100, 200));//每日新增课程数
        baseMapper.insert(statisticsDaily);

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //查询时间范围内的所有数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end).select("date_calculated", type);//根据管理员不同的需求显示不同的列

        List<StatisticsDaily> statisticsDailyList = baseMapper.selectList(wrapper);

        //对获取到的数据进行封装，Echarts需要返回一个数组，所以需要将获取到的数据封装到ArrayList集合中
        List<String> date_calculated = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        //遍历statisticsDailyList中数据存放到各自的集合中
        for (StatisticsDaily statisticsDaily : statisticsDailyList) {
            date_calculated.add(statisticsDaily.getDateCalculated());//封装日期数据
            //因为取到的具体数据不固定所以需要判断传回来的字段
            switch (type){
                case "login_num": numDataList.add(statisticsDaily.getLoginNum());
                    break;
                case "register_num": numDataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "video_view_num": numDataList.add(statisticsDaily.getVideoViewNum());
                    break;
                default: numDataList.add(statisticsDaily.getCourseNum());
                    break;
            }
        }

        //最终封装在Map集合中返回前端
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculated", date_calculated);
        map.put("numDataList", numDataList);

        return map;
    }
}
