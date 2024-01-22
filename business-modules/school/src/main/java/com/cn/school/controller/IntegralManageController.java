package com.cn.school.controller;


import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.school.config.Constant;
import com.cn.school.entity.IntegralManageDo;
import com.cn.school.entity.UserDo;
import com.cn.school.service.impl.IntegralManageServiceImpl;
import com.cn.school.service.impl.SysDataDictionaryServiceImpl;
import com.cn.school.service.impl.UserServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.pub.core.utils.CalculateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Controller
@RequestMapping("/school/integralManage")
public class IntegralManageController extends BaseController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private IntegralManageServiceImpl integralManageServiceImpl;

    @Autowired
    private SysDataDictionaryServiceImpl sysDataDictionaryServiceImpl;
    /**
     * 添加一笔提现记录
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addIntegralManage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addIdentity(@RequestBody IntegralManageDo integralManage){
        try{

            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            UserDo byId = userService.getById(id);
            String phone = byId.getPhone();
            String integralFee = integralManage.getIntegralFee();
            /**
             * 提现积分不得小于阀值
             */
            String integral_manage_num = sysDataDictionaryServiceImpl.getSysBaseParam("integral_manage_num", "integral_manage_num");
            BigDecimal cal_integral_manage_num = CalculateUtil.cal(new StringBuilder().append(integral_manage_num).append("-").append(integralFee).toString());
            if(cal_integral_manage_num.compareTo(BigDecimal.ZERO)>0){
                return AjaxResult.error("提现积分必须大于"+integral_manage_num+"！");
            }
            String integral = byId.getIntegral();
            BigDecimal cal = CalculateUtil.cal(new StringBuilder().append(integral).append("-").append(integralFee).toString());
            if(cal.compareTo(BigDecimal.ZERO)<0){
                return AjaxResult.error("当前可用积分"+integral+"，提现金额不得大于积分量！");
            }
            if(StringUtils.isNotBlank(phone)){
                integralManage.setPhone(phone);
            }

            String identityName = byId.getIdentityName();
            if(StringUtils.isNotBlank(identityName)){
                integralManage.setIdentityName(identityName);
            }
            integralManage.setUserId(id);

            integralManage.setIntegral(integral);
            /**
             * 状态初始化
             */
            integralManage.setStatus(0);
            integralManageServiceImpl.save(integralManage);
            /**
             * 进行积分扣减
             */
            userService.addIntegral(byId.getId(),integralManage.getIntegralFee(), Constant.TicketAddStatus.DEL);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 审核积分
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/verifyIntegral", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult check(@RequestBody IntegralManageDo integralManage){
        try{

            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            UserDo byId = userService.getById(id);
            String phone = byId.getPhone();
            String integralFee = integralManage.getIntegralFee();
            integralManage.setSysUserId(id);
            integralManage.setSysIdentityName(byId.getIdentityName());
            /**
             * 状态已审核
             */
            integralManage.setStatus(1);
            integralManageServiceImpl.updateById(integralManage);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 用户获取提现审核分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getPageList(@RequestBody IntegralManageDo req){
        try{
            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            req.setUserId(id);
            List<IntegralManageDo> pageList = integralManageServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 用户获取提现审核分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getSysPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getSysPageList(@RequestBody IntegralManageDo req){
        try{
            List<IntegralManageDo> pageList = integralManageServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}

