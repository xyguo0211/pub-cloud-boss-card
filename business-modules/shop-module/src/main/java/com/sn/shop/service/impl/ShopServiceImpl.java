package com.sn.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pub.core.utils.PageUtils;
import com.pub.core.web.controller.BaseController;
import com.sn.shop.entity.ShopDo;
import com.sn.shop.mapper.ShopMapper;
import com.sn.shop.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-04-28
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, ShopDo> implements IShopService {

    public List<ShopDo> listPage(ShopDo req) {
        LambdaQueryWrapper<ShopDo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDo::getProductName, req.getProductName());
        PageUtils.startPage();
        List<ShopDo> list = list(wrapper);
        return list;

    }
}
