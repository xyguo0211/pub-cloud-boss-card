package com.cn.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.entity.TripProductCarRelationDo;
import com.cn.school.mapper.TripProductCarRelationMapper;
import com.cn.school.service.ITripProductCarRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品和车次关联关系表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Service
public class TripProductCarRelationServiceImpl extends ServiceImpl<TripProductCarRelationMapper, TripProductCarRelationDo> implements ITripProductCarRelationService {

    @Autowired
    private TripProductCarRelationMapper tripProductCarRelationMapper;

    public List<Map> findTrips(Integer trip_area_id, String data) {
       return tripProductCarRelationMapper.findTrips(trip_area_id, data);
    }

    public void addTripCarDoProduct(TripProductCarRelationDo tripProductCarRelationDo) throws Exception {
        Integer carId = tripProductCarRelationDo.getCarId();
        if(carId==null){
            throw  new BusinessException("请选择车次！");
        }
        Integer productId = tripProductCarRelationDo.getProductId();
        if(productId==null){
            throw  new BusinessException("请选择产品！");
        }
        QueryWrapper<TripProductCarRelationDo> wq=new QueryWrapper<>();
        wq.eq("car_id",tripProductCarRelationDo.getCarId());
        wq.eq("product_id",tripProductCarRelationDo.getProductId());
        TripProductCarRelationDo one = getOne(wq);
        if(one!=null){
            throw  new BusinessException("该车次和车票已配置！");
        }
        tripProductCarRelationDo.setCreateTime(new Date());
        save(tripProductCarRelationDo);
    }
}
