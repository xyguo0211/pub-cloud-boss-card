package com.cn.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 登录帐号表 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@Mapper
public interface SysUserMapper extends BaseMapper<User> {

}
