package com.pub.core.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/4/11.
 */
public class RandomUtil {

    /**
     * <p>获取100-999之间的随机数</p>
     */
    public static int getRandomLenThree(){
        return (int)(Math.random()*900)+100;
    }
    /**
     *
     */
    public static int getRandomLenOne(){
        return (int)(Math.random()*10);
    }



    /**
     * <p>获取100-999之间的随机数</p>
     */
    public static int getRandomLenTwo(){
        return (int)(Math.random()*50)+20;
    }



    /**
     * <p>获取1-5之间的随机数</p>
     */
    public static int getRandomLen10(){
        return (int)(Math.random()*3)+1;
    }
    /**
     * <p>获取2-5之间的随机数</p>
     */
    public static int getRandomLen20(){
        return (int)(Math.random()*3)+2;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen50(){
        return (int)(Math.random()*4)+3;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen80(){
        return (int)(Math.random()*5)+3;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen30(){
        return (int)(Math.random()*5)+3;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen100(){
        return (int)(Math.random()*3)+6;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen200(){
        return (int)(Math.random()*10)+5;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen500(){
        return (int)(Math.random()*10)+15;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen1000(){
        return (int)(Math.random()*10)+23;
    }
    public static int getRandomLen2500(){
        return (int)(Math.random()*10)+28;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen5000(){
        return (int)(Math.random()*10)+30;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen10000(){
        return (int)(Math.random()*20)+40;
    }
    /**
     * <p>获取3-8之间的随机数</p>
     */
    public static int getRandomLen100000000(){
        return (int)(Math.random()*30)+100;
    }
    /**
     * <p>获取1000-9999之间的随机数</p>
     */
    public static int getRandomLenFours(){
        return (int)(Math.random()*9000)+1000;
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            int randomLenTwo = getRandomLen100();
            System.out.println(randomLenTwo);
        }
    }

    public static int getRandomDistance(String weight_distance) {
        BigDecimal cal_10 = CalculateUtil.cal(new StringBuilder("10-").append(weight_distance).toString());
        if(cal_10.compareTo(new BigDecimal(0))>=0){
            return getRandomLen10();
        }
        BigDecimal cal_20 = CalculateUtil.cal(new StringBuilder("20-").append(weight_distance).toString());
        if(cal_20.compareTo(new BigDecimal(0))>=0){
            return getRandomLen20();
        }

        BigDecimal cal_30 = CalculateUtil.cal(new StringBuilder("30-").append(weight_distance).toString());
        if(cal_30.compareTo(new BigDecimal(0))>=0){
            return getRandomLen30();
        }

        BigDecimal cal_50 = CalculateUtil.cal(new StringBuilder("50-").append(weight_distance).toString());
        if(cal_50.compareTo(new BigDecimal(0))>=0){
            return getRandomLen50();
        }
        BigDecimal cal_80 = CalculateUtil.cal(new StringBuilder("80-").append(weight_distance).toString());
        if(cal_80.compareTo(new BigDecimal(0))>=0){
            return getRandomLen80();
        }
        BigDecimal cal_100 = CalculateUtil.cal(new StringBuilder("100-").append(weight_distance).toString());
        if(cal_100.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_200 = CalculateUtil.cal(new StringBuilder("200-").append(weight_distance).toString());
        if(cal_200.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_500 = CalculateUtil.cal(new StringBuilder("500-").append(weight_distance).toString());
        if(cal_500.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_1000 = CalculateUtil.cal(new StringBuilder("1000-").append(weight_distance).toString());
        if(cal_1000.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_2500 = CalculateUtil.cal(new StringBuilder("2500-").append(weight_distance).toString());
        if(cal_2500.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_5000 = CalculateUtil.cal(new StringBuilder("5000-").append(weight_distance).toString());
        if(cal_5000.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_10000 = CalculateUtil.cal(new StringBuilder("10000-").append(weight_distance).toString());
        if(cal_10000.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        BigDecimal cal_1000000000000 = CalculateUtil.cal(new StringBuilder("100000000000-").append(weight_distance).toString());
        if(cal_1000000000000.compareTo(new BigDecimal(0))>=0){
            return getRandomLen100();
        }
        return getRandomLen100();
    }
}
