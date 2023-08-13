package com.cn.offline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.offline.entity.OnlineTransactionHistoryDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 交易记录 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-12
 */
@Mapper
public interface OnlineTransactionHistoryMapper extends BaseMapper<OnlineTransactionHistoryDo> {

}
