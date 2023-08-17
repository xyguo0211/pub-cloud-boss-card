package com.cn.school.service.impl;

import com.cn.school.entity.TripProductCarRelationDo;
import com.cn.school.mapper.TripProductCarRelationMapper;
import com.cn.school.service.ITripProductCarRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
