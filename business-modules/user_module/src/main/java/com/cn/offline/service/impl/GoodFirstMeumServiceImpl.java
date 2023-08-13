package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.GoodFirstMeumMapper;
import com.cn.offline.service.IGoodFirstMeumService;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private OfflineFilePathOnlineConfig filePathOnlineConfig;



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




    public String uploadImage(MultipartFile file) throws Exception {
        //存储文件路径
        String root = filePathOnlineConfig.getRoot();
        String baseUrl = filePathOnlineConfig.getBaseUrl();
        String shipDfsPath = getDfsPath(root);
        File extracted = extracted(file, shipDfsPath);
        String absolutePath = extracted.getAbsolutePath();
        String[] split = absolutePath.split(root);
        if(split.length>1){
            return baseUrl+split[1];
        }
        return null;
    }


    public static String getDfsPath(String root) {

        return  root +  "/"
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

    public void addFirstCard(GoodFirstMeumDo req) {
        Date createTime = new Date();
        req.setCreateTime(createTime);
        req.setUpdateTime(createTime);
        String image = req.getCardImgeUrl();
        String[] split = image.split(filePathOnlineConfig.getBaseUrl());
        if(split.length>1){
            req.setCardImgeUrl(split[1]);
        }else{
            req.setCardImgeUrl(split[0]);
        }
        save(req);
        List<GoodFirstMeumEquirementsDo> listEquirements = req.getListEquirements();
        if(listEquirements!=null&&listEquirements.size()>0){
            for (GoodFirstMeumEquirementsDo listEquirement : listEquirements) {
                listEquirement.setFirstId(req.getId());
                listEquirement.setCreateTime(createTime);
                listEquirement.setUpdateTime(createTime);
            }
            goodFirstMeumEquirementsServiceImpl.saveBatch(listEquirements);
        }

    }

    public void updateFirstCard(GoodFirstMeumDo req) {
        Date createTime = new Date();
        req.setUpdateTime(createTime);
        String image = req.getCardImgeUrl();
        if(StringUtils.isNotBlank(image)&&image.contains(":")){
            String[] split = image.split(filePathOnlineConfig.getBaseUrl());
            if(split.length>1){
                req.setCardImgeUrl(split[1]);
            }else{
                req.setCardImgeUrl(split[0]);
            }
        }

        updateById(req);
        QueryWrapper<GoodFirstMeumEquirementsDo> rm=new QueryWrapper<>();
        rm.eq("first_id",req.getId());
        goodFirstMeumEquirementsServiceImpl.remove(rm);
        List<GoodFirstMeumEquirementsDo> listEquirements = req.getListEquirements();
        if(listEquirements!=null&&listEquirements.size()>0){
            for (GoodFirstMeumEquirementsDo listEquirement : listEquirements) {
                listEquirement.setFirstId(req.getId());
                listEquirement.setCreateTime(createTime);
                listEquirement.setUpdateTime(createTime);
            }
            goodFirstMeumEquirementsServiceImpl.saveBatch(listEquirements);
        }
    }

    public GoodFirstMeumDo getByIdEntity(Integer id) {
        GoodFirstMeumDo byId = getById(id);
        String cardImgeUrl = byId.getCardImgeUrl();
        if(StringUtils.isNotBlank(cardImgeUrl)){
            byId.setCardImgeUrl(filePathOnlineConfig.getBaseUrl()+cardImgeUrl);
        }
        QueryWrapper<GoodFirstMeumEquirementsDo> wq=new QueryWrapper<>();
        wq.eq("first_id",id);
        List<GoodFirstMeumEquirementsDo> list = goodFirstMeumEquirementsServiceImpl.list(wq);
        byId.setListEquirements(list);
        return byId;
    }

    public void deleteById(Integer id) {
        removeById(id);
        QueryWrapper<GoodFirstMeumEquirementsDo> first_rm=new QueryWrapper<>();
        first_rm.eq("first_id",id);
        goodFirstMeumEquirementsServiceImpl.remove(first_rm);
        QueryWrapper<GoodSecondCountryDo> sencond_rm=new QueryWrapper<>();
        sencond_rm.eq("first_id",id);
        goodSecondCountryServiceImpl.remove(sencond_rm);
        QueryWrapper<GoodThirdRateDo> third_rm=new QueryWrapper<>();
        third_rm.eq("first_id",id);
        goodThirdRateServiceImpl.remove(third_rm);
    }

    public List<GoodFirstMeumDo> getPageList(GoodFirstMeumDo req) {
        QueryWrapper<GoodFirstMeumDo> wq=new QueryWrapper<>();
        String name = req.getCardName();
        if(StringUtils.isNotBlank(name)){
            wq.like("card_name", name);
        }
        BaseController.startPage();
        List<GoodFirstMeumDo> list = list(wq);
        for (GoodFirstMeumDo goodFirstMeumDo : list) {
            goodFirstMeumDo.setCardImgeUrl(filePathOnlineConfig.getBaseUrl()+goodFirstMeumDo.getCardImgeUrl());
        }
        return list;
    }
}
