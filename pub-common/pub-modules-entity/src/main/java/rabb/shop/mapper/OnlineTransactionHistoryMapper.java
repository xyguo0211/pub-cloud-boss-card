package rabb.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rabb.shop.entity.OnlineTransactionHistoryDo;

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
