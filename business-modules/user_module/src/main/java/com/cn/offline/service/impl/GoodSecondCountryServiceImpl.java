package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.GoodSecondCountryMapper;
import com.cn.offline.service.IGoodSecondCountryService;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Service
public class GoodSecondCountryServiceImpl extends ServiceImpl<GoodSecondCountryMapper, GoodSecondCountryDo> implements IGoodSecondCountryService {

    @Autowired
    private GoodThirdRateServiceImpl goodThirdRateServiceImpl;
    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;
    @Autowired
    private OfflineCountryServiceImpl offlineCountryServiceImpl;

    @Autowired
    private OfflineFilePathOnlineConfig filePathOnlineConfig;;

    public void addSecondCountry(GoodSecondCountryDo req)throws  Exception {

        QueryWrapper<GoodSecondCountryDo> wq_un=new QueryWrapper<>();
        wq_un.eq("first_id",req.getFirstId());
        wq_un.eq("country_id",req.getCountryId());
        GoodSecondCountryDo one = getOne(wq_un);
        if(one!=null){
            throw new BusinessException("已存在国家配置");
        }
        Integer countryId = req.getCountryId();
        OfflineCountryDo offlineCountryDo = offlineCountryServiceImpl.getById(countryId);
        req.setCountryName(offlineCountryDo.getCountryName());
        req.setCountryImage(offlineCountryDo.getImageUrl());
        Date createTime = new Date();
        req.setCreateTime(createTime);
        req.setUpdateTime(createTime);
        save(req);
    }

    public void updateSecondCountry(GoodSecondCountryDo req) {
        Date createTime = new Date();
        req.setUpdateTime(createTime);
        updateById(req);
    }

    public void deleteById(Integer id) {
        removeById(id);
        QueryWrapper<GoodThirdRateDo> third_rm=new QueryWrapper<>();
        third_rm.eq("second_id",id);
        goodThirdRateServiceImpl.remove(third_rm);
    }

    public List<GoodSecondCountryDo> getPageList(GoodSecondCountryDo req) {
        QueryWrapper<GoodSecondCountryDo> wq=new QueryWrapper<>();
        Integer firstId = req.getFirstId();
        if(firstId!=null){
            wq.eq("first_id", firstId);
        }
        String name = req.getCountryName();
        if(StringUtils.isNotBlank(name)){
            wq.like("country_name", name);
        }
        BaseController.startPage();
        List<GoodSecondCountryDo> list = list(wq);
        for (GoodSecondCountryDo goodSecondCountryDo : list) {
            GoodFirstMeumDo goodFirstMeumDo = goodFirstMeumServiceImpl.getById(goodSecondCountryDo.getFirstId());
            goodSecondCountryDo.setCountryImage(filePathOnlineConfig.getBaseUrl()+"/"+goodSecondCountryDo.getCountryImage());
            if(goodFirstMeumDo!=null){
                goodSecondCountryDo.setCardName(goodFirstMeumDo.getCardName());
            }

        }
        return list;
    }
}
