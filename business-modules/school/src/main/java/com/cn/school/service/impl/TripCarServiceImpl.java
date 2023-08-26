package com.cn.school.service.impl;

import com.cn.school.config.Constant;
import com.cn.school.entity.TripCarDo;
import com.cn.school.mapper.TripCarMapper;
import com.cn.school.service.ITripCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.exception.BusinessException;
import org.springframework.stereotype.Service;

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
}
