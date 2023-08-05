package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.exception.BusinessException;
import com.pub.core.utils.StringUtils;
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
                String cardImgeUrl = goodFirstMeumDo.getCardImgeUrl();
                if(StringUtils.isNotBlank(cardImgeUrl)){
                    goodFirstMeumDo.setCardImgeUrl(filePathOnlineConfig.getBaseUrl()+cardImgeUrl);
                }
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




    public String uploadImage(MultipartFile file) throws Exception {
        //存储文件路径
        String root = filePathOnlineConfig.getRoot();
        String shipDfsPath = getDfsPath(root);
        File extracted = extracted(file, shipDfsPath);
        String absolutePath = extracted.getAbsolutePath();
        String[] split = absolutePath.split(root);
        if(split.length>1){
            return split[1];
        }
       return null;
    }


    public static void main(String[] args) {
        String str="/home/image/card/af53a382ee0447caa2e7be7ee3180a03/询价请求参数确认.xls";
        String[] split = str.split("/home/image/card");
        System.out.println(split[1]);
    }

    public static String getDfsPath(String service) {

        return  service + "/" + UUID.randomUUID().toString().replace("-", "")+"/"
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
