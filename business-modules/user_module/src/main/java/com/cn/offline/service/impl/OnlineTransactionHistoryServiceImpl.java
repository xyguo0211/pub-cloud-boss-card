package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.entity.OnlineTransactionHistoryDo;
import com.cn.offline.mapper.OnlineTransactionHistoryMapper;
import com.cn.offline.service.IOnlineTransactionHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易记录 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-12
 */
@Service
public class OnlineTransactionHistoryServiceImpl extends ServiceImpl<OnlineTransactionHistoryMapper, OnlineTransactionHistoryDo> implements IOnlineTransactionHistoryService {

}
