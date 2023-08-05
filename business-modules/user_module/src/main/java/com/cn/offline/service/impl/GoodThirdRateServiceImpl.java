package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.entity.GoodThirdCardTypeDo;
import com.cn.offline.entity.GoodThirdRateDo;
import com.cn.offline.mapper.GoodThirdRateMapper;
import com.cn.offline.service.IGoodThirdRateService;
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
public class GoodThirdRateServiceImpl extends ServiceImpl<GoodThirdRateMapper, GoodThirdRateDo> implements IGoodThirdRateService {

    @Autowired
    private GoodThirdCardTypeServiceImpl goodThirdCardTypeServiceImpl;

    public void addThirdRate(GoodThirdRateDo req) {
        Date createTime = new Date();
        req.setCreateTime(createTime);
        req.setUpdateTime(createTime);
        save(req);
        List<GoodThirdCardTypeDo> listCardType = req.getListCardType();
        if(listCardType!=null&&listCardType.size()>0){
            for (GoodThirdCardTypeDo goodThirdCardTypeDo : listCardType) {
                goodThirdCardTypeDo.setCreateTime(createTime);
                goodThirdCardTypeDo.setUpdateTime(createTime);
                goodThirdCardTypeDo.setThirdId(req.getId());
            }
            goodThirdCardTypeServiceImpl.saveBatch(listCardType);
        }
    }

    public void updateThirdRate(GoodThirdRateDo req) {
        Date createTime = new Date();
        req.setUpdateTime(createTime);
        updateById(req);
        QueryWrapper<GoodThirdCardTypeDo> rw=new QueryWrapper<>();
        rw.eq("third_id",req.getId());
        goodThirdCardTypeServiceImpl.remove(rw);
        List<GoodThirdCardTypeDo> listCardType = req.getListCardType();
        if(listCardType!=null&&listCardType.size()>0){
            for (GoodThirdCardTypeDo goodThirdCardTypeDo : listCardType) {
                goodThirdCardTypeDo.setCreateTime(createTime);
                goodThirdCardTypeDo.setUpdateTime(createTime);
                goodThirdCardTypeDo.setThirdId(req.getId());
            }
            goodThirdCardTypeServiceImpl.saveBatch(listCardType);
        }
    }

    public GoodThirdRateDo getEntityById(Integer id) {
        GoodThirdRateDo byId = getById(id);
        QueryWrapper<GoodThirdCardTypeDo> rw = new QueryWrapper<>();
        rw.eq("third_id", id);
        List<GoodThirdCardTypeDo> list = goodThirdCardTypeServiceImpl.list(rw);
        byId.setListCardType(list);
        return byId;
    }

    public void deleteById(Integer id) {
        removeById(id);
    }
}
