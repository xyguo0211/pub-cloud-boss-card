package rabb.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import rabb.shop.entity.OnlineUserDo;

/**
 * <p>
 * 在线用户表 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Mapper
public interface OnlineUserMapper extends BaseMapper<OnlineUserDo> {

}
