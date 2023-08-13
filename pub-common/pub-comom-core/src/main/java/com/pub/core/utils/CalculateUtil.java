package com.pub.core.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateUtil {


    public static BigDecimal cal(String str) {
        // 对表达式进行预处理，并简单验证是否是正确的表达式
        // 存放处理后的表达式
        List<String> list = new ArrayList<>();
        char[] arr = str.toCharArray();
        // 存放数字临时变量
        StringBuffer tmpStr = new StringBuffer();
        for (char c : arr) {
            // 如果是数字或小数点，添加到临时变量中
            if (c >= '0' && c <= '9') {
                tmpStr.append(c);
            } else if (c == '.') {
                if (tmpStr.indexOf(".") > 0) {
                    throw new RuntimeException("非法字符");
                }
                tmpStr.append(c);
            }
            // 如果是加减乘除或者括号，将数字临时变量和运算符依次放入list中
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
                if (tmpStr.length() > 0) {
                    list.add(tmpStr.toString());
                    tmpStr.setLength(0);
                }
                list.add(c + "");
            }
            // 如果是空格，跳过
            else if (c == ' ') {
                continue;
            } else {
                throw new RuntimeException("非法字符");
            }
        }
        if (tmpStr.length() > 0) {
            list.add(tmpStr.toString());
        }
        // 初始化后缀表达式
        List<String> strList = new ArrayList<>();
        // 运算过程中，使用了两次栈结构，第一次是将中缀表达式转换为后缀表达式，第二次是计算后缀表达式的值
        Stack<String> stack = new Stack<>();
        // 声明临时变量，存放出栈元素
        String tmp;
        // 1. 将中缀表达式转换为后缀表达式
        for (String s : list) {
            // 如果是左括号直接入栈
            if (s.equals("(")) {
                stack.push(s);
            }
            // 如果是右括号，执行出栈操作，依次添加到后缀表达式中，直到出栈元素为左括号，左括号和右括号都不添加到后缀表达式中
            else if (s.equals(")")) {
                while (!(tmp = stack.pop()).equals("(")) {
                    strList.add(tmp);
                }
            }
            // 如果是加减乘除，弹出所有优先级大于或者等于该运算符的栈顶元素（栈中肯定没有右括号，认为左括号的优先级最低），然后将该运算符入栈
            else if (s.equals("*") || s.equals("/")) {
                while (!stack.isEmpty()) {
                    // 取出栈顶元素
                    tmp = stack.peek();
                    if (tmp.equals("*") || tmp.equals("/")) {
                        stack.pop();
                        strList.add(tmp);
                    } else {
                        break;
                    }
                }
                stack.push(s);
            } else if (s.equals("+") || s.equals("-")) {
                while (!stack.isEmpty()) {
                    // 取出栈顶元素
                    tmp = stack.peek();
                    if (!tmp.equals("(")) {
                        stack.pop();
                        strList.add(tmp);
                    } else {
                        break;
                    }
                }
                stack.push(s);
            }
            // 如果是数字，直接添加到后缀表达式中
            else {
                strList.add(s);
            }
        }
        // 最后依次出栈，放入后缀表达式中
        while (!stack.isEmpty()) {
            strList.add(stack.pop());
        }
        // 2.计算后缀表达式的值
        Stack<BigDecimal> newStack = new Stack<>();
        for (String s : strList) {
            // 若遇运算符，则从栈中退出两个元素，先退出的放到运算符的右边，后退出的放到运算符左边，
            // 运算后的结果再进栈，直到后缀表达式遍历完毕
            if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
                BigDecimal b1 = newStack.pop();
                BigDecimal b2 = newStack.pop();
                switch (s) {
                    case "+":
                        newStack.push(b2.add(b1));
                        break;
                    case "-":
                        newStack.push(b2.subtract(b1));
                        break;
                    case "*":
                        newStack.push(b2.multiply(b1));
                        break;
                    case "/":
                        newStack.push(b2.divide(b1, 9, BigDecimal.ROUND_HALF_UP));
                        break;
                }
            }
            // 如果是数字，入栈
            else {
                newStack.push(new BigDecimal(s));
            }
        }
        // 最后，栈中仅有一个元素，就是计算结果
        BigDecimal peek = newStack.peek();
        if(peek.compareTo(BigDecimal.valueOf(0))==0){
            return BigDecimal.valueOf(0);
        }
        return peek;
    }

    /**
     * 两个整数相除，保留两位小数
     * @return
     */
    public static String getDouble2(Integer totalDays,Integer countOrder){
        float num= (float)totalDays/countOrder;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        return df.format(num);//返回的是String类型
    }

    /**
     *
     * @param x 对数
     * @param y 为底数
     * @return
     */
    public static double log(double x, double y) {
        return Math.log(x) / Math.log(y);
    }

    public static int getPercentSize(int size ,int percent){
        int rtn= size*percent/100;
        return rtn;
    }


    /**
     * 计算车辆的提柜天数的几何平均数
     */
    public static double  getXhxWharfCarConfigAvg(List<Map> mapList){
        double rlt=1;
        double num=0;
        for (Map map : mapList) {
            num = Double.valueOf(map.get("ct").toString());
            rlt*=num;//实现相乘功能
        }
        rlt=Math.pow(rlt,1.0/Double.valueOf(mapList.size()));//实现开三次方功能，注意这里必须写成1.0/3.0，写成1/3会转成int
        return rlt;
    }

    List<Integer> list = new ArrayList<Integer>();

  public  static int  getDiff(String nearNum ,List<Map> dvalue){
      Map map1 = dvalue.get(0);
      Integer ct = Integer.valueOf(map1.get("ct").toString());
      Integer n_Num = Integer.valueOf(nearNum);
      int diffNum = Math.abs(ct - n_Num);
      int j=0;
      for (int i = 0; i < dvalue.size(); i++) {
          Map map_temp =  dvalue.get(i);
          Integer ct_temp = Integer.valueOf(map_temp.get("ct").toString());
          int diffNumTemp = Math.abs(ct_temp - n_Num);
          if (diffNumTemp < diffNum) {
              diffNum = diffNumTemp;
              j=i;
          }
          
      }
    return j;
  }


    /**
     * 四舍五入  i 为0 保留整数 1为保留一位小数
     * @param bd
     */

  public static String getIntValue(BigDecimal bd,int i){
      String bd1  = bd.setScale( i, BigDecimal.ROUND_HALF_UP ).toString();
      //结果：11  BigDecimal 转成 String 四舍五入保留整数
      return bd1;
  }

    /**
     * 比较两个值大小
     *flag = -1,表示bigdemical1小于bigdemical2；
     * flag = 0,表示bigdemical1等于bigdemical2；
     * flag = 1,表示bigdemical1大于bigdemical2；
     */

    public static int compareAToB(String A,String B){
        if(A.startsWith("-")){
            A="0"+A;
        }
        if(B.startsWith("-")){
            B="0"+B;
        }
       StringBuilder sb=new StringBuilder(A).append("-").append(B);
       System.out.println(sb.toString());
        BigDecimal cal = CalculateUtil.cal(sb.toString());
        int i = cal.compareTo(BigDecimal.valueOf(0));

        return i;
    }

    public static String getFuzhi(String A){

        if(A.startsWith("-")){
            StringBuilder sb=new StringBuilder("(0").append(A).append(")");
            return sb.toString();
        }
        return A;
    }


    /**
     * @param a 单数  32
     * @param b 总数  145
     * a / b    计算百分比32/145
     * @return 22.07%
     */
    public static String getPercent(BigDecimal a, BigDecimal b){
        String percent =
                b == null ? "-" :
                        b.compareTo(new BigDecimal(0)) == 0 ? "-":
                                a == null ? "0.00%" :
                                        a.multiply(new BigDecimal(100)).divide(b,2,BigDecimal.ROUND_HALF_UP) + "%";
        return percent;
    }


    /**
     * 计算百分比 保留两位小数
     * 被除数/除数*100% 保留两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数 不为零
     * @return percentage
     */
    public static BigDecimal calculatePercentage(int dividend, int divisor) {
        BigDecimal percentage = BigDecimal.ZERO;
        if (divisor < dividend) {
            return new BigDecimal(100);
        }
        if (divisor != 0) {
            percentage = BigDecimal.valueOf(dividend).divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }
        return percentage.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算百分比 保留两位小数
     * 被除数/除数*100% 保留两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数 不为零
     * @return percentage
     */
    public static BigDecimal calculatePercentage(BigDecimal dividend, BigDecimal divisor) {
        BigDecimal percentage = BigDecimal.ZERO;
        if (divisor.compareTo(dividend) < 0) {
            return new BigDecimal(100);
        }
        if(divisor.compareTo(percentage)>0)  {
            percentage =dividend.divide(divisor, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }
        return percentage.setScale(2, RoundingMode.HALF_UP);
    }


    public static String getPrice(JSONObject jsonObject01){
        String priceStr="0";
        if(jsonObject01!=null){
            Object price = jsonObject01.get("price");
            if(price!=null){
                if(price.toString().startsWith("-")){
                    //如果是负数
                    StringBuilder sb=new StringBuilder("(0").append(price).append(")");
                    priceStr=sb.toString();
                }else{
                    priceStr=price+"";
                }

            }
        }
        return priceStr;
    }
    public static String getPriceNew(JSONObject jsonObject01){
        String priceStr="0";
        if(jsonObject01!=null){
            Object price = jsonObject01.get("price");
            if(price!=null){
                priceStr=price.toString();
            }
        }
        return priceStr;
    }




    public static String getStrAll(Map<Integer, JSONObject> mapAll, List<Integer> list){
        StringBuilder stringBuilder=new StringBuilder("(");
        StringBuilder stringBuilderlog=new StringBuilder("费用组成====");
        for (Integer i : list) {
            stringBuilderlog.append(i).append(",");
            JSONObject jsonObject01 = mapAll.get(i);
            stringBuilder.append(CalculateUtil.getPrice(jsonObject01)).append("+");
        }
        String s = stringBuilder.toString();
        if(s.endsWith("+")){
            //在末尾加0.这样更符合
            stringBuilderlog.append("末尾加0");
            stringBuilder.append("0)");
            return stringBuilder.toString();
        }
        return null;

    }




    /**
     * 生产订单号方法
     * @param prefix
     * @return
     */
    public static String nextSn(String prefix){

        String hashCodeStr=UUID.randomUUID().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateStr = sdf.format(new Date());

        Random r=new Random();
        int rand=r.nextInt(90) +10;

        String noStr=prefix+dateStr+rand;

        hashCodeStr=hashCodeStr.replace("-", "");
        int dif=28-noStr.length();
        int hashCodeLen=hashCodeStr.length();
        if(dif<hashCodeLen){

            hashCodeStr=hashCodeStr.substring(0, dif);

        }else if(dif>=hashCodeLen){

            int size=dif-hashCodeLen;
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<size;i++){
                sb.append("0");
            }
            hashCodeStr=sb.toString()+hashCodeStr;

        }
        noStr=noStr+hashCodeStr;

        return noStr;
    }

    /**
     * 费用取整数值
     * @param rateEngine01
     */
    public static void getPriceInt(JSONObject rateEngine01) {
        try {
            if(rateEngine01!=null){
                Object price = rateEngine01.get("price");
                if(price!=null){
                    String price_str=price+"";
                    if(StringUtils.isNotBlank(price_str)){
                        BigDecimal bd = new BigDecimal(price_str );
                        String bd1  = bd.setScale( 0, BigDecimal.ROUND_HALF_UP ).toString();
                        //结果：11  BigDecimal 转成 String 四舍五入保留整数
                        rateEngine01.put("price",bd1);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    /**
     * 依据经纬度计算两点之间的距离 GetDistance:(). <br/>
     *
     * @param lat1 1点的纬度
     * @param lng1 1点的经度
     * @param lat2 2点的纬度
     * @param lng2 2点的经度
     * @return 距离 单位 m
     * @author chiwei
     * @since JDK 1.6
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s*1000;
    }
    /**
     * 依据经纬度计算两点之间的距离 GetDistance:(). <br/>
     *
     * @param lat1 1点的纬度
     * @param lng1 1点的经度
     * @param lat2 2点的纬度
     * @param lng2 2点的经度
     * @return 距离 单位 m
     * @author chiwei
     * @since JDK 1.6
     */
    public static double getDistanceKM(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }
    /**
     * 依据经纬度计算两点之间的距离 GetDistance:(). <br/>
     *
     * @param lat1 1点的纬经度
     * @param lat2 2点的纬经度
     * @return 距离 单位 m
     * @author chiwei
     * @since JDK 1.6
     */
    public static double getDistanceKM(String lat1,  String lat2) {
        String[] split1 = lat1.split(",");
        String[] split2 = lat2.split(",");
        double radLat1 = getRadian(Double.valueOf(split1[0]));
        double radLat2 = getRadian(Double.valueOf(split2[0]));
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(Double.valueOf(split1[1])) - getRadian(Double.valueOf(split2[1]));// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }
    private static double EARTH_RADIUS = 6378.137;// 单位千米

    /**
     * 角度弧度计算公式 rad:(). <br/>
     * <p>
     * 360度=2π π=Math.PI
     * <p>
     * x度 = x*π/360 弧度
     *
     * @param
     * @return
     * @author chiwei
     * @since JDK 1.6
     */
    private static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    public static void main(String ar[]) {
        double a =getDistance(43.838635, 125.37147, 43.838642, 125.372);
        System.out.println(a);
    }



}
