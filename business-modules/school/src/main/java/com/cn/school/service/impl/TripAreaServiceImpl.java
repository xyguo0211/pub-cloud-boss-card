package com.cn.school.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripProductCarRelationDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.mapper.TripAreaMapper;
import com.cn.school.service.ITripAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行程表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Service
public class TripAreaServiceImpl extends ServiceImpl<TripAreaMapper, TripAreaDo> implements ITripAreaService {

    @Autowired
    private TripProductServiceImpl tripProductService;
    @Autowired
    private TripCarServiceImpl tripCarServiceImpl;
    @Autowired
    private TripProductCarRelationServiceImpl tripProductCarRelationServiceImpl;

    public void addFirstCard(TripAreaDo tripAreaDo) {
        tripAreaDo.setCreateTime(new Date());
        save(tripAreaDo);
    }

    public Map<String,List<String>> startTrips() {
        List<TripAreaDo> list = list();
        Map<String,List<String>> map=new HashedMap();
        for (TripAreaDo tripAreaDo : list) {
            String cityName = tripAreaDo.getCityName();
            List<String> tripAreaDos = map.get(cityName);
            if(tripAreaDos==null){
                tripAreaDos=new ArrayList<>();
                map.put(cityName,tripAreaDos);
            }
            String origin = tripAreaDo.getOrigin();
            tripAreaDos.add(origin);
        }
        return map;
    }

    public Map<String, List<TripAreaDo>> endTrips(String startTrips) {
        QueryWrapper<TripAreaDo> wq=new QueryWrapper<>();
        wq.eq("origin",startTrips);
        List<TripAreaDo> list = list(wq);
        Map<String,List<TripAreaDo>> map=new HashedMap();
        for (TripAreaDo tripAreaDo : list) {
            String cityName = tripAreaDo.getCityName();
            List<TripAreaDo> tripAreaDos = map.get(cityName);
            if(tripAreaDos==null){
                tripAreaDos=new ArrayList<>();
                map.put(cityName,tripAreaDos);
            }
            tripAreaDos.add(tripAreaDo);
        }
        return map;
    }

    public List<TripAreaDo> gethotTrips() {
        QueryWrapper<TripAreaDo> wq=new QueryWrapper<>();
        wq.eq("is_hot",1);
        List<TripAreaDo> list = list(wq);
        return list;
    }

    public List<TripAreaDo> getPageList(TripAreaDo req) {
        return null;
    }

    public List<Map> findTrips(Integer trip_area_id,String data) {
        return tripProductCarRelationServiceImpl.findTrips(trip_area_id, data);
    }
}
