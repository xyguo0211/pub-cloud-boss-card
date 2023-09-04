package com.cn.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.mapper.TripProductMapper;
import com.cn.school.service.ITripProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 班次表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Service
public class TripProductServiceImpl extends ServiceImpl<TripProductMapper, TripProductDo> implements ITripProductService {

    @Autowired
    private TripAreaServiceImpl tripAreaServiceImpl;

    public List<TripProductDo> getPageList(TripProductDo req) {
        QueryWrapper<TripProductDo> wq=new QueryWrapper<>();
        Integer deleteStatus = req.getDeleteStatus();
        if(deleteStatus !=null){
            wq.eq("delete_status",deleteStatus);
        }
        BaseController.startPage();
        List<TripProductDo> list = list(wq);
        for (TripProductDo tripProductDo : list) {
            Integer tripAreaId = tripProductDo.getTripAreaId();
            TripAreaDo tripAreaDo = tripAreaServiceImpl.getById(tripAreaId);
            tripProductDo.setOrigin(tripAreaDo.getOrigin());
            tripProductDo.setDestination(tripAreaDo.getDestination());
        }
        return list;
    }

    public void addTripProductDo(TripProductDo tripProductDo) throws Exception{
        QueryWrapper<TripProductDo> wq=new QueryWrapper<>();
        wq.eq("trip_area_id",tripProductDo.getTripAreaId());
        wq.eq("fee",tripProductDo.getFee());
        TripProductDo one = getOne(wq);
        if(one!=null){
            throw new BusinessException("航线此价格已配置！");
        }
        tripProductDo.setCreateTime(new Date());
        tripProductDo.setDeleteStatus(9);
        save(tripProductDo);
    }

    public void editTripAreaDo(TripProductDo tripProductDo) throws Exception{
        QueryWrapper<TripProductDo> wq=new QueryWrapper<>();
        wq.eq("trip_area_id",tripProductDo.getTripAreaId());
        wq.eq("fee",tripProductDo.getFee());
        wq.ne("id",tripProductDo.getId());
        TripProductDo one = getOne(wq);
        if(one!=null){
            throw new BusinessException("航线此价格已配置！");
        }
        updateById(tripProductDo);
    }
}
