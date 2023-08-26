package com.cn.school.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.config.Constant;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.service.impl.TripCarServiceImpl;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.pub.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
@RefreshScope
public class OrderCancelTask {
    private Logger log= LoggerFactory.getLogger("WeatherTaskLogger");




    /**
     * 任务开关
     */
    @Value("${task.order_switch}")
    private Integer order_switch;

    @Autowired
    private TripCarServiceImpl tripCarServiceImpl;

    @Autowired
    private TripOrderServiceImpl tripOrderService;

    @Scheduled(cron="${task.weather_cron}")
    public void opertDellDataRedis() throws ParseException {
        log.info("开始执行15分钟未支付订单取消任务");
        if(order_switch!= 9){
            log.info("15分钟未支付订单取消开关未打开");
            return;
        }
        String dateFromat = DateUtils.dateTimeNow();
        try {
            /**
             * 查询那些超过分钟未10分钟未支付订单
             */
            QueryWrapper<TripOrderDo> wq=new QueryWrapper<>();
            /**
             * 订单状态   0 初始  1成功  -1 失败
             */
            wq.eq("status",0);
            /**
             * 小于等于
             */
            wq.lt("create_time",DateUtils.addMinutes(new Date(),-15));
            List<TripOrderDo> list = tripOrderService.list(wq);
            if(list!=null&&list.size()>0){
                for (TripOrderDo tripOrderDo : list) {
                    tripOrderDo.setStatus(-1);
                    /**
                     * 释放车票
                     */
                    Integer ticketStatus = tripOrderDo.getTicketStatus();
                    if(ticketStatus!=9){
                        tripOrderDo.setTicketStatus(9);
                        Integer num = tripOrderDo.getNum();
                        Integer carId = tripOrderDo.getCarId();
                        tripCarServiceImpl.addTicket(carId,num, Constant.TicketAddStatus.ADD);
                    }
                    tripOrderService.updateById(tripOrderDo);

                }
            }

        }catch (Exception e){
                e.printStackTrace();
                log.error(dateFromat+"15分钟未支付订单取消，异常+"+e.getMessage());
            }
        }


}
