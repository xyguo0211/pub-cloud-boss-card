package com.sn.online.mapper;

import com.sn.online.entity.OnlineUserDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
