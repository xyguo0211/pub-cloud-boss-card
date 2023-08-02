package com.sn.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.exception.BusinessException;
import com.sn.online.common.OnlineConstants;
import com.sn.online.config.FilePathOnlineConfig;
import com.sn.online.entity.*;
import com.sn.online.entity.dto.OnlineOrderSubmitDto;
import com.sn.online.mapper.GoodFirstMeumMapper;
import com.sn.online.service.IGoodFirstMeumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Service
public class GoodFirstMeumServiceImpl extends ServiceImpl<GoodFirstMeumMapper, GoodFirstMeumDo> implements IGoodFirstMeumService {

    @Autowired
    private GoodSecondCountryServiceImpl  goodSecondCountryServiceImpl;
    @Autowired
    private GoodThirdRateServiceImpl  goodThirdRateServiceImpl;
    @Autowired
    private GoodThirdCardTypeServiceImpl  goodThirdCardTypeServiceImpl;
    @Autowired
    private GoodFirstMeumEquirementsServiceImpl  goodFirstMeumEquirementsServiceImpl;
    @Autowired
    private OnlineOrderInfoServiceImpl  onlineOrderInfoServiceImpl;
    @Autowired
    private OnlineOrderInfoImageServiceImpl  onlineOrderInfoImageServiceImpl;

    @Autowired
    private FilePathOnlineConfig filePathOnlineConfig;



    public List<GoodFirstMeumDo> getFirstPage() {
        QueryWrapper<GoodFirstMeumDo> wq_goodfirstmeum=new QueryWrapper<>();
        List<GoodFirstMeumDo> list = list(wq_goodfirstmeum);
        if(list!=null&&list.size()>0){
            for (GoodFirstMeumDo goodFirstMeumDo : list) {
                QueryWrapper<GoodFirstMeumEquirementsDo> wq_equirements=new QueryWrapper<>();
                wq_equirements.eq("first_id",goodFirstMeumDo.getId());
                List<GoodFirstMeumEquirementsDo> listequirements = goodFirstMeumEquirementsServiceImpl.list(wq_equirements);
                if(listequirements!=null&&listequirements.size()>0){
                    goodFirstMeumDo.setListEquirements(listequirements);
                }
                QueryWrapper<GoodSecondCountryDo> wq_goodsecond=new QueryWrapper<>();
                wq_goodsecond.eq("first_id",goodFirstMeumDo.getId());
                List<GoodSecondCountryDo> list_goodsecond = goodSecondCountryServiceImpl.list(wq_goodsecond);
                if(list_goodsecond!=null&&list_goodsecond.size()>0){
                    goodFirstMeumDo.setListSencond(list_goodsecond);
                    for (GoodSecondCountryDo goodSecondCountryDo : list_goodsecond) {
                        QueryWrapper<GoodThirdRateDo> wq_third=new QueryWrapper<>();
                        wq_third.eq("second_id",goodSecondCountryDo.getId());
                        wq_third.eq("first_id",goodSecondCountryDo.getFirstId());
                        List<GoodThirdRateDo> list_rate = goodThirdRateServiceImpl.list(wq_third);
                        if(list_rate!=null&&list_rate.size()>0){
                            goodSecondCountryDo.setListThird(list_rate);
                            for (GoodThirdRateDo goodThirdRateDo : list_rate) {
                                QueryWrapper<GoodThirdCardTypeDo> wq_car_type=new QueryWrapper<>();
                                wq_car_type.eq("third_id",goodThirdRateDo.getId());
                                List<GoodThirdCardTypeDo> list_GoodThirdCardTypeDo = goodThirdCardTypeServiceImpl.list(wq_car_type);
                                if(list_GoodThirdCardTypeDo!=null&&list_GoodThirdCardTypeDo.size()>0){
                                    goodThirdRateDo.setListCardType(list_GoodThirdCardTypeDo);
                                }
                            }
                        }
                    }
                }

            }
        }
        return list;

    }


    public GoodThirdRateDo openOrder(Integer third_id) {
        GoodThirdRateDo goodThirdRateDo = goodThirdRateServiceImpl.getById(third_id);
        Integer secondId = goodThirdRateDo.getSecondId();
        GoodSecondCountryDo goodSecondCountryDo = goodSecondCountryServiceImpl.getById(secondId);
        goodThirdRateDo.setGoodSecondCountryDo(goodSecondCountryDo);
        Integer firstId = goodThirdRateDo.getFirstId();
        GoodFirstMeumDo goodFirstMeumDo = getById(firstId);
        QueryWrapper<GoodFirstMeumEquirementsDo> wq_equirements=new QueryWrapper<>();
        wq_equirements.eq("first_id",goodFirstMeumDo.getId());
        List<GoodFirstMeumEquirementsDo> listequirements = goodFirstMeumEquirementsServiceImpl.list(wq_equirements);
        if(listequirements!=null&&listequirements.size()>0){
            goodFirstMeumDo.setListEquirements(listequirements);
        }
        goodThirdRateDo.setGoodFirstMeumDo(goodFirstMeumDo);
        QueryWrapper<GoodThirdCardTypeDo> wq_car_type=new QueryWrapper<>();
        wq_car_type.eq("third_id",goodThirdRateDo.getId());
        List<GoodThirdCardTypeDo> list_GoodThirdCardTypeDo = goodThirdCardTypeServiceImpl.list(wq_car_type);
        if(list_GoodThirdCardTypeDo!=null&&list_GoodThirdCardTypeDo.size()>0){
            goodThirdRateDo.setListCardType(list_GoodThirdCardTypeDo);
        }
        return goodThirdRateDo;
    }

    public void submitOrder(OnlineOrderSubmitDto onlineOrderSubmitDto) {
        User currentUser = UserContext.getCurrentUser();
        OnlineOrderInfoDo onlineOrderInfoDo=new OnlineOrderInfoDo();
        BeanUtils.copyProperties(onlineOrderSubmitDto,onlineOrderInfoDo);
        Date createTime = new Date();
        onlineOrderInfoDo.setCreateTime(createTime);
        onlineOrderInfoDo.setUserId(Integer.valueOf(currentUser.getId()));
        onlineOrderInfoDo.setOrderStatus(OnlineConstants.orderStats.initial);
        onlineOrderInfoServiceImpl.save(onlineOrderInfoDo);
        List<String> images = onlineOrderSubmitDto.getImages();
        List<OnlineOrderInfoImageDo> list_OnlineOrderInfoImageDo=new ArrayList<>();
        if(images!=null&&images.size()>0){
            for (String image : images) {
                OnlineOrderInfoImageDo onlineOrderInfoImageDo=new OnlineOrderInfoImageDo();
                onlineOrderInfoImageDo.setOrderId(onlineOrderInfoDo.getId());
                onlineOrderInfoImageDo.setCreateTime(createTime);
                onlineOrderInfoImageDo.setImageUrl(image);
                list_OnlineOrderInfoImageDo.add(onlineOrderInfoImageDo);
            }
            onlineOrderInfoImageServiceImpl.saveBatch(list_OnlineOrderInfoImageDo);
        }
    }

    public String uploadImage(MultipartFile file) throws Exception {
        //存储文件路径
        long time = new Date().getTime();
        String shipDfsPath = getDfsPath(filePathOnlineConfig.getRoot(), "online_card",time+"identitycarddowm");
        File extracted = extracted(file, shipDfsPath);
        String absolutePath = extracted.getAbsolutePath();
        return absolutePath;
    }

    public static String getDfsPath(String service, String module, String path) {

        return "/" + service + "/" + module + "/"+ "/" + path + "/"
                + UUID.randomUUID().toString().replace("-", "")+"/"
                ;

    }
    public  File extracted(MultipartFile multipartFile, String path) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        File localFile = new File(path + originalFilename);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        multipartFile.transferTo(localFile);
        if (multipartFile.isEmpty()) {
            multipartFile.getInputStream().close();
        }
        return localFile;
    }
}
