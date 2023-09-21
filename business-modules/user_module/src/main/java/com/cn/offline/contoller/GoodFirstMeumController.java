package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.service.impl.GoodFirstMeumServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
/*import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;*/
/*import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;*/
/*import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Controller
@RequestMapping("/offline/goodFirstMeumDo")
public class GoodFirstMeumController extends BaseController {

    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;

    @Value("${imageName}")
    private String imageName;

    /**
     * 上传文件
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImage(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            /**
             *
             */
            String[] split = imageName.split("#");
            boolean isImage=false;
            for (String s : split) {
                if(fileName.endsWith(s)){
                    isImage=true;
                    break;
                }
            }
            if(!isImage){
                return AjaxResult.error("上传文件非图片格式 !");
            }
            String url= goodFirstMeumServiceImpl.uploadImage(file);
            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 上传文件
     * @return
     */
    /*@TimingLog
    @RequestMapping(value = "/uploadImageTest01", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImageTest01(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            *//**
             *
             *//*
            String[] split = imageName.split("#");
            boolean isImage=false;
            for (String s : split) {
                if(fileName.endsWith(s)){
                    isImage=true;
                    break;
                }
            }
            if(!isImage){
                return AjaxResult.error("上传文件非图片格式 !");
            }
            URL urlpath = ClassLoader.getSystemResource("lib/opencv_java341.dll");
            System.load(urlpath.getPath());
            String url= goodFirstMeumServiceImpl.uploadImageTest(file);
            // 读取图像
            Mat image = Imgcodecs.imread(url);
            // 加载OpenCV库
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            // 转换图像为灰度图像
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // 计算图像的梯度
            Mat gradientImage = new Mat();
            Imgproc.Sobel(grayImage, gradientImage, CvType.CV_64F, 1, 1);

            // 计算图像的清晰度
            Scalar mean = Core.mean(gradientImage);
            double sharpness = mean.val[0];

            // 输出图像的清晰度
            System.out.println("Image Sharpness: " + sharpness);


            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }*/
    /**
     * 上传文件
     * @return
     */
   /* @TimingLog
    @RequestMapping(value = "/uploadImageTest", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImageTest(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            *//**
             *
             *//*
            String[] split = imageName.split("#");
            boolean isImage=false;
            for (String s : split) {
                if(fileName.endsWith(s)){
                    isImage=true;
                    break;
                }
            }
            if(!isImage){
                return AjaxResult.error("上传文件非图片格式 !");
            }
            URL urlpath = ClassLoader.getSystemResource("lib/opencv_java341.dll");
            System.load(urlpath.getPath());
            String url= goodFirstMeumServiceImpl.uploadImageTest(file);

            String path="C:\\Users\\Administrator\\Desktop\\card\\";

            opencv_core.Mat srcImage = opencv_imgcodecs.imread(url);
            opencv_core.Mat dstImage = new opencv_core.Mat();

            //转化为灰度图
            opencv_imgproc.cvtColor(srcImage, dstImage, opencv_imgproc.COLOR_BGR2GRAY);
            //在gray目录下生成灰度图片
            opencv_imgcodecs.imwrite(path +"gray\\"+ "gray-" + fileName, dstImage);

            opencv_core.Mat laplacianDstImage = new opencv_core.Mat();
            //阈值太低会导致正常图片被误断为模糊图片，阈值太高会导致模糊图片被误判为正常图片
            opencv_imgproc.Laplacian(dstImage, laplacianDstImage, opencv_core.CV_64F);
            //在laplacian目录下升成经过拉普拉斯掩模做卷积运算的图片
            opencv_imgcodecs.imwrite(path + "lap-" + fileName, laplacianDstImage);

            //矩阵标准差
            opencv_core.Mat stddev = new opencv_core.Mat();

            //求矩阵的均值与标准差
            opencv_core.meanStdDev(laplacianDstImage, new opencv_core.Mat(), stddev);

            *//**
             * 检测图片清晰度   标准差越大说明图像质量越好
             * @param baseResult  base64 图片
             * @param fileName
             * @return
             * @throws IOException
             *//*
            double aDouble = stddev.createIndexer().getDouble();
            // 输出图像的清晰度
            System.out.println("Image Sharpness: " + aDouble);


            return AjaxResult.success(aDouble);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }*/

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addFirstCard", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addFirstCard(@RequestBody GoodFirstMeumDo req){
        try{
            goodFirstMeumServiceImpl.addFirstCard(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/updateFirstCard", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult updateFirstCard(@RequestBody GoodFirstMeumDo req){
        try{
            goodFirstMeumServiceImpl.updateFirstCard(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult getById(@RequestParam Integer id){
        try{
            GoodFirstMeumDo byIdEntity = goodFirstMeumServiceImpl.getByIdEntity(id);
            return AjaxResult.success(byIdEntity);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult deleteById(@RequestParam Integer id){
        try{
             goodFirstMeumServiceImpl.deleteById(id);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult getPageList(@RequestBody GoodFirstMeumDo req){
        try{
            List<GoodFirstMeumDo> pageList = goodFirstMeumServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}

