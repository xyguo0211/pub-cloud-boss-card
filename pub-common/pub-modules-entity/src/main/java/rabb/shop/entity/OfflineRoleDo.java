package rabb.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Data
@TableName("offline_role")
public class OfflineRoleDo extends Model<OfflineRoleDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //0 不可以接单   1可以接单
    private Integer isOrder;

    private Date createTime;

    /**
     * 角色名称
     */
    private String name;


}
