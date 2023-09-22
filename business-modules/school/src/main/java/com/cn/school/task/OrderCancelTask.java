package com.cn.school.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.config.Constant;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.service.impl.TripCarServiceImpl;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.cn.school.util.SendSmsTx;
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

    /**
     * 余票不足提醒按钮
     */
    @Value("${task.tickets_switch}")
    private Integer tickets_switch;
    /**
     * 余票不足通知管理员手机号
     */
    @Value("${task.tickets_phone}")
    private String tickets_phone;
    /**
     * 余票不足多少时候提醒
     */
    @Value("${task.tickets_num}")
    private Integer tickets_num;
    /**
     * 发车前一小时提醒开关
     */
    @Value("${task.car_switch}")
    private Integer car_switch;
    /**
     * 发车前多少小时提醒
     */
    @Value("${task.car_time}")
    private Integer car_time;

    @Autowired
    private TripCarServiceImpl tripCarServiceImpl;

    @Autowired
    private TripOrderServiceImpl tripOrderService;

    @Autowired
    private SendSmsTx sendSmsTx;

    /**
     * 未支付成功订单关闭按钮
     * @throws ParseException
     */
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

    /**
     *  余票不足发送通知
      * @throws ParseException
     */
    @Scheduled(cron="${task.tickets_cron}")
    public void insufficientRemainingTickets() throws ParseException {
        log.info("开始执行余票不足发送通知任务");
        if(tickets_switch!= 9){
            log.info("余票不足发送通知开关未打开");
            return;
        }
        String dateFromat = DateUtils.dateTimeNow();
        try {
            /**
             * 查询那些超过分钟未10分钟未支付订单
             */
            QueryWrapper<TripCarDo> wq=new QueryWrapper<>();
            /**
             * 订单状态  -1 未通知  9 已通知
             */
            wq.eq("notice_status",-1);
            wq.ne("sell_num",0);
            /**
             * 小于等于
             */
            List<TripCarDo> list = tripCarServiceImpl.list(wq);
            for (TripCarDo tripCarDo : list) {
                Integer sellNum = tripCarDo.getSellNum();
                Integer orderNum = tripCarDo.getOrderNum();
                if(orderNum-sellNum<=tickets_num){
                    StringBuilder sb=new StringBuilder(tripCarDo.getCarNumber())
                            .append("车牌号，发车时间:").
                            append(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,tripCarDo.getStartTime()))
                            .append("余票数量不足"+tickets_num+"张！");
                    //短信通知管理员
                    sendSmsTx.sendMsgNoticeSystem(tickets_phone,sb.toString());
                }
                tripCarDo.setNoticeStatus(9);
                tripCarServiceImpl.updateById(tripCarDo);
            }

        }catch (Exception e){
                e.printStackTrace();
                log.error(dateFromat+"15分钟未支付订单取消，异常+"+e.getMessage());
            }
        }

    /**
     *  发车前一小时通知,已废弃，原因车牌号不固定
      * @throws ParseException
     */
    /*@Scheduled(cron="${task.car_cron}")*/
  /*  public void carStartTime() throws ParseException {
        log.info("开始执行发车前一小时通知任务");
        if(car_switch!= 9){
            log.info("发车前一小时通知开关未打开");
            return;
        }
        String dateFromat = DateUtils.dateTimeNow();
        try {
            *//**
             * 查询那些超过分钟未10分钟未支付订单
             *//*
            QueryWrapper<TripOrderDo> wq=new QueryWrapper<>();
            *//**
             * 订单状态   0 初始  1成功  -1 失败
             *//*
            wq.eq("status",1);
            *//**
             * 未提醒
             *//*
            wq.eq("notice_status",-1);
            *//**
             * 未上车
             *//*
            wq.eq("on_car_status",0);
            List<TripOrderDo> list = tripOrderService.list(wq);
            if(list!=null&&list.size()>0){
                for (TripOrderDo tripOrderDo : list) {
                    tripOrderDo.setNoticeStatus(9);
                    *//**
                     * 发送短信通知
                     *//*
                    Date starTime = tripOrderDo.getStarTime();
                    if(DateUtils.addHours(new Date(),1).after(starTime)){
                        String identityName = tripOrderDo.getIdentityName();
                        sendSmsTx.sendMsgCarTime(tripOrderDo.getPhone(),identityName,DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,starTime));
                        tripOrderService.updateById(tripOrderDo);
                    }
                }
            }

        }catch (Exception e){
                e.printStackTrace();
                log.error(dateFromat+"15分钟未支付订单取消，异常+"+e.getMessage());
        }
    }

*/
}
