package com.pub.core.utils;


import java.util.UUID;


public class RandomCode {
	/**  
     * 生成32位随机数
     * @return string  
     */
	 public static String getUUID() {  
		return UUID.randomUUID().toString().trim().replaceAll("-", "");   
 }
}
