package com.cn.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.config.Constant;
import com.cn.school.entity.*;
import com.cn.school.mapper.TripCarMapper;
import com.cn.school.service.ITripCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.DateUtils;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车次 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Service
public class TripCarServiceImpl extends ServiceImpl<TripCarMapper, TripCarDo> implements ITripCarService {

    @Autowired
    private TripProductServiceImpl tripProductService;

    @Autowired
    private TripProductCarRelationServiceImpl tripProductCarRelationServiceImpl;

    @Autowired
    private TripAreaServiceImpl tripAreaServiceImpl;

    @Autowired
    private TripOrderServiceImpl tripOrderServiceImpl;

    /**
     * 一定要加上锁，避免并发出错
     * @param carId
     * @param num
     * @param type  -1是减票
     */
    public synchronized void addTicket(Integer carId, Integer num, Integer type) throws Exception {
        TripCarDo tripCarDo = getById(carId);
        Integer sellNum = tripCarDo.getSellNum();
        if(Constant.TicketAddStatus.DEL==type){
            //如果减票,查看余票释放充足
            Integer orderNum = tripCarDo.getOrderNum();
            if( orderNum-sellNum-num<0){
                //说明没票了
                throw  new BusinessException("余票不足！请刷新购买页面");
            }
            tripCarDo.setSellNum(sellNum+num);
            updateById(tripCarDo);

        }else{
            tripCarDo.setSellNum(sellNum+num);
            updateById(tripCarDo);
        }
    }

    public List<TripCarDo> getCarCheck() {
        QueryWrapper<TripCarDo> wq=new QueryWrapper<>();
        wq.like("start_time", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,new Date()));
        List<TripCarDo> list = list(wq);
        for (TripCarDo tripCarDo : list) {
            //设置已核销的订单和核销出发点和目的地
            Integer id = tripCarDo.getId();
            QueryWrapper<TripProductCarRelationDo> wqTripProductCarRelationDo=new QueryWrapper<>();
            wqTripProductCarRelationDo.eq("car_id",id);
            List<TripProductCarRelationDo> listTripProductCarRelationDo = tripProductCarRelationServiceImpl.list(wqTripProductCarRelationDo);
            if(listTripProductCarRelationDo!=null&&listTripProductCarRelationDo.size()>0){
                List<TripProductDo> listTripProductDo=new ArrayList<>();
                for (TripProductCarRelationDo tripProductCarRelationDo : listTripProductCarRelationDo) {
                    Integer productId = tripProductCarRelationDo.getProductId();
                    TripProductDo tripProductDo = tripProductService.getById(productId);
                    Integer tripAreaId = tripProductDo.getTripAreaId();
                    TripAreaDo tripAreaDo = tripAreaServiceImpl.getById(tripAreaId);
                    tripProductDo.setOrigin(tripAreaDo.getOrigin());
                    tripProductDo.setDestination(tripAreaDo.getDestination());
                    listTripProductDo.add(tripProductDo);
                }
                tripCarDo.setListTripProductDo(listTripProductDo);
            }
            /**
             * 设置已核销和未核销订单量
             */
            QueryWrapper<TripOrderDo> wq_car=new QueryWrapper<>();
            wq_car.eq("car_id",tripCarDo.getId());
            wq_car.eq("status",Constant.OrderStatus.SUCESS);
            List<TripOrderDo> listTripOrderDo = tripOrderServiceImpl.list(wq_car);
            List<TripOrderDo> waitcarList=new ArrayList<>();
            int oncarTicks=0;
            for (TripOrderDo tripOrderDo : listTripOrderDo) {
                oncarTicks=oncarTicks+tripOrderDo.getOnCarStatus();
                Integer onCarStatus = tripOrderDo.getOnCarStatus();
                Integer num = tripOrderDo.getNum();
                if(onCarStatus<num){
                    //说明未上车
                    waitcarList.add(tripOrderDo);
                }
            }
            tripCarDo.setOncarTicks(oncarTicks);
            /**
             * 设置未上车的数据
             */
            tripCarDo.setWaitcarList(waitcarList);

        }
        return list;
    }

    public List<TripCarDo> getPageList(TripCarDo req) {
        QueryWrapper<TripCarDo> wq=new QueryWrapper<>();
        Integer isDeparted = req.getIsDeparted();
        if(isDeparted!=null){
            wq.eq("is_departed",isDeparted);
        }
        String carNumber = req.getCarNumber();
        if(StringUtils.isNotBlank(carNumber)){
            wq.eq("car_number",carNumber);
        }
        String startTime = req.getStartTimeStr();
        if(StringUtils.isNotBlank(startTime)){
            wq.like("start_time",startTime);
        }
        BaseController.startPage();
        List<TripCarDo> list = list(wq);
        for (TripCarDo tripCarDo : list) {
            Integer id = tripCarDo.getId();
            QueryWrapper<TripProductCarRelationDo> wqTripProductCarRelationDo=new QueryWrapper<>();
            wqTripProductCarRelationDo.eq("car_id",id);
            List<TripProductCarRelationDo> listTripProductCarRelationDo = tripProductCarRelationServiceImpl.list(wqTripProductCarRelationDo);
            if(listTripProductCarRelationDo!=null&&listTripProductCarRelationDo.size()>0){
                List<TripProductDo> listTripProductDo=new ArrayList<>();
                for (TripProductCarRelationDo tripProductCarRelationDo : listTripProductCarRelationDo) {
                    Integer productId = tripProductCarRelationDo.getProductId();
                    TripProductDo tripProductDo = tripProductService.getById(productId);
                    Integer tripAreaId = tripProductDo.getTripAreaId();
                    TripAreaDo tripAreaDo = tripAreaServiceImpl.getById(tripAreaId);
                    tripProductDo.setOrigin(tripAreaDo.getOrigin());
                    tripProductDo.setDestination(tripAreaDo.getDestination());
                    listTripProductDo.add(tripProductDo);
                }
                tripCarDo.setListTripProductDo(listTripProductDo);
            }
        }
        return list;
    }

    public void addTripCarDo(TripCarDo tripCarDo) {
        tripCarDo.setIsDeparted(-1);
        tripCarDo.setCreateTime(new Date());
        /**
         * 设置已售为0
         */
        tripCarDo.setSellNum(0);
        /**
         * 余票通知未通知管理员，未通知
         */
        tripCarDo.setNoticeStatus(-1);
        save(tripCarDo);
    }

    public void editTripCarDo(TripCarDo tripCarDo) throws Exception{
        /**
         * 已发车，已售票的不允许修改时间
         */
        TripCarDo byId = getById(tripCarDo.getId());
        Integer isDeparted = byId.getIsDeparted();
        if(9==isDeparted){
            throw  new BusinessException("已发车不允许修改状态！");
        }
        Integer sellNum = byId.getSellNum();
        if(sellNum>0){
            //已售票，不允许修改发车时间，不允许票数量少于已售
            if(tripCarDo.getOrderNum()!=null&&tripCarDo.getOrderNum()<sellNum){
                throw  new BusinessException("已售票不能少于票总数！");
            }
            Date startTimedb = byId.getStartTime();
            String s = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startTimedb);
            Date startTime= tripCarDo.getStartTime();
            if(startTime!=null){
                String s1 = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startTime);
                if(!s.equals(s1)){
                    throw  new BusinessException("已售票不能修改发车时间！");
                }
            }
        }
        updateById(tripCarDo);
    }

    public  List<TripProductDo> tripCarDoProductCheckBox() {
        QueryWrapper<TripProductDo> wq=new QueryWrapper<>();
        wq.eq("delete_status",9);
        wq.orderByDesc("trip_area_id");
        List<TripProductDo> list = tripProductService.list(wq);
        for (TripProductDo tripProductDo : list) {
            Integer tripAreaId = tripProductDo.getTripAreaId();
            TripAreaDo byId = tripAreaServiceImpl.getById(tripAreaId);
            String origin = byId.getOrigin();
            tripProductDo.setOrigin(origin);
            String destination = byId.getDestination();
            tripProductDo.setDestination(destination);
        }
        return list;
    }

    public void deleteCarPrductRelation(Integer productId,Integer carId) {
        QueryWrapper<TripProductCarRelationDo> wq=new QueryWrapper<>();
        wq.eq("car_id",carId);
        wq.eq("product_id",productId);
        tripProductCarRelationServiceImpl.remove(wq);
    }
}
