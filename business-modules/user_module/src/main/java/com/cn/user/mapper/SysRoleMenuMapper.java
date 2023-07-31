package com.cn.user.mapper;

import com.cn.user.entity.SysRoleMenuDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuDo> {

    @Select("<script>"+
            "SELECT t.menu_id,t.fun_value,m.menu_name,m.menu_url,m.parent_id FROM " +
            "   sys_role_menu t LEFT JOIN sys_menu m on t.menu_id=m.id" +
            "    WHERE t.role_id=#{id}" +
            "</script>")
    List<Map> getMenuByUserId(Long id);
}
