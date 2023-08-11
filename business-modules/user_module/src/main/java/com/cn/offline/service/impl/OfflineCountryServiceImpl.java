package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.offline.entity.OfflineCountryDo;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.mapper.OfflineCountryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.service.IOfflineCountryService;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 (EIP) 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-11
 */
@Service
public class OfflineCountryServiceImpl extends ServiceImpl<OfflineCountryMapper, OfflineCountryDo> implements IOfflineCountryService {

    public List<OfflineCountryDo> getPageList(OfflineCountryDo req) {
        QueryWrapper<OfflineCountryDo> wq=new QueryWrapper<>();
        String name = req.getCountryName();
        if(StringUtils.isNotBlank(name)){
            wq.like("country_name", name);
        }
        BaseController.startPage();
        List<OfflineCountryDo> list = list(wq);
        return list;
    }
}
