package rabb.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 订单图片回复表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Data
@TableName("online_order_info_reply_image")
public class OnlineOrderInfoReplyImageDo extends Model<OnlineOrderInfoReplyImageDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Integer reply_id;

    private String imageUrl;



}
