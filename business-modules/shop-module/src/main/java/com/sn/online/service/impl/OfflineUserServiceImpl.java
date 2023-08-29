package com.sn.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rabb.shop.entity.OfflineRoleDo;
import rabb.shop.entity.OfflineUserDo;
import rabb.shop.entity.OnlineOrderInfoDo;
import rabb.shop.mapper.OfflineUserMapper;

import java.util.*;

/**
 * <p>
 * 离线用户表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Slf4j
@Service
public class OfflineUserServiceImpl extends ServiceImpl<OfflineUserMapper, OfflineUserDo> implements IService<OfflineUserDo> {

    @Autowired
    private OfflineRoleServiceImpl offlineRoleService;

    /**
     * 订单分配客服处理
     * @param onlineOrderInfoDo
     */
    public void divideOrder(OnlineOrderInfoDo onlineOrderInfoDo) {
        QueryWrapper<OfflineRoleDo> wq_role=new QueryWrapper<>();
        wq_role.eq("is_order",1);
        List<OfflineRoleDo> list = offlineRoleService.list(wq_role);
        List<Integer> listId=new ArrayList<>();
        if(list!=null&&list.size()>0){
            for (OfflineRoleDo offlineRoleDo : list) {
                Integer id = offlineRoleDo.getId();
                listId.add(id);
            }
            QueryWrapper<OfflineUserDo> wq_user=new QueryWrapper<>();
            wq_user.eq("is_black",9);
            wq_user.in("role_id",listId);
            wq_user.lt("start_time",new Date());
            wq_user.ge("end_time",new Date());
            List<OfflineUserDo> listOfflineUserDo = list(wq_user);
            if(listOfflineUserDo!=null&&listOfflineUserDo.size()>0){
                //说明此时有客服在线
                if(listOfflineUserDo.size()==1){
                    //只有一人不需要分配
                    OfflineUserDo offlineUserDo = listOfflineUserDo.get(0);
                    onlineOrderInfoDo.setOfflineUserId(offlineUserDo.getId());
                    onlineOrderInfoDo.setOfflineUserName(offlineUserDo.getName());
                }else{
                    String uuid = UUID.randomUUID().toString();
                    int has_index = Math.abs(uuid.hashCode()) % listOfflineUserDo.size() ;
                    OfflineUserDo offlineUserDo = listOfflineUserDo.get(has_index);
                    onlineOrderInfoDo.setOfflineUserId(offlineUserDo.getId());
                    onlineOrderInfoDo.setOfflineUserName(offlineUserDo.getName());
                }

            }else{
                /**
                 * 查询离线的客服
                 */
                QueryWrapper<OfflineUserDo> wq_user_ofline=new QueryWrapper<>();
                wq_user_ofline.eq("is_black",9);
                wq_user_ofline.in("role_id",listId);
                List<OfflineUserDo> listOfflineUserDoOffline = list(wq_user_ofline);
                if(listOfflineUserDoOffline!=null&&listOfflineUserDoOffline.size()>0){
                    if(listOfflineUserDoOffline.size()==1){
                        //只有一人不需要分配
                        OfflineUserDo offlineUserDo = listOfflineUserDoOffline.get(0);
                        onlineOrderInfoDo.setOfflineUserId(offlineUserDo.getId());
                        onlineOrderInfoDo.setOfflineUserName(offlineUserDo.getName());
                    }else{
                        String uuid = UUID.randomUUID().toString();
                        int has_index = Math.abs(uuid.hashCode()) % listOfflineUserDoOffline.size() ;
                        OfflineUserDo offlineUserDo = listOfflineUserDoOffline.get(has_index);
                        onlineOrderInfoDo.setOfflineUserId(offlineUserDo.getId());
                        onlineOrderInfoDo.setOfflineUserName(offlineUserDo.getName());
                    }
                }else{
                    //只能分配给主管了
                    OfflineUserDo byId = getById(1);
                    onlineOrderInfoDo.setOfflineUserId(byId.getId());
                    onlineOrderInfoDo.setOfflineUserName(byId.getName());
                }
            }

        }else{
            //只能分配给主管了
            OfflineUserDo byId = getById(1);
            onlineOrderInfoDo.setOfflineUserId(byId.getId());
            onlineOrderInfoDo.setOfflineUserName(byId.getName());
        }
    }
}
