package com.cn.school.mapper;

import com.cn.school.entity.UserDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDo> {

}
