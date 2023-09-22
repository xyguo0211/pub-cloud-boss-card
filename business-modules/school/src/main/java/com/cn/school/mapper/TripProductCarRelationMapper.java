package com.cn.school.mapper;

import com.cn.school.entity.TripProductCarRelationDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品和车次关联关系表 Mapper 接口
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Mapper
public interface TripProductCarRelationMapper extends BaseMapper<TripProductCarRelationDo> {


    @Select({
            "<script>",
            " SELECT t.start_time,t.id as car_id ,t.order_num-t.sell_num as sell_num ,n.fee,n.id as product_id,k.city_name,k.origin,k.destination FROM trip_car t LEFT JOIN trip_product_car_relation m on t.id=m.car_id LEFT JOIN trip_product n on m.product_id=n.id LEFT JOIN trip_area k on n.trip_area_id=k.id    ",
            "   WHERE LEFT(t.start_time ,10)= #{data} AND n.trip_area_id=#{trip_area_id} AND n.delete_status=9   AND t.start_time > NOW() ",
            "</script>"
    })
    List<Map> findTrips(@Param(value="trip_area_id")Integer trip_area_id, @Param(value="data")String data);
}
