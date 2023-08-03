package com.cn.offline.mapper;

import com.cn.offline.entity.OfflineUserDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 离线用户表 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Mapper
public interface OfflineUserMapper extends BaseMapper<OfflineUserDo> {

}
