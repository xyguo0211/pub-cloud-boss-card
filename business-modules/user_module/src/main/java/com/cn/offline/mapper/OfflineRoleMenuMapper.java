package com.cn.offline.mapper;

import com.cn.offline.entity.OfflineRoleMenuDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Mapper
public interface OfflineRoleMenuMapper extends BaseMapper<OfflineRoleMenuDo> {

    @Select({
            "<script>",
            " SELECT m.menu_name, m.menu_url from offline_role_menu r LEFT JOIN offline_menu m on r.menu_id=m.id WHERE r.role_id=#{roleId} ORDER BY m.sort  ",
            "</script>"
    })
    List<Map> getRoleMeumList(Integer roleId);
}
