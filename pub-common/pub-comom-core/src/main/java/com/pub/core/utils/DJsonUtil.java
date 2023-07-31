package com.pub.core.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DJsonUtil {

	private static Logger logger = LoggerFactory.getLogger(DJsonUtil.class);

	/**
	 * 根据json返回实体对象
	 * 
	 * @param jsongObj
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T getBeanFromJson(String jsongObj, Class clazz)
			throws Exception {
		return (T) JSON.parseObject(jsongObj, clazz);
	}

	/**
	 * 根据json串生成MAP
	 */
	public static Map getMapFromJsObject(String pString) throws Exception {
		Map rtnMap = JSON.parseObject(pString, Map.class);
		return rtnMap;
	}
	
	

	/**
	 * 数组生成json
	 * 
	 * @param pArray
	 * @return
	 * @throws Exception
	 */
	public static String getJsonFromObect(Object[] pArray) throws Exception {
		Object rtn = JSON.toJSON(pArray);
		return rtn.toString();
	}

	/**
	 * 根据json串返回对象
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object getObjectFromJsonStr(String obj) throws Exception {
		return JSON.parse(obj);
	}

	/**
	 * 根据object生成json串
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String getJsonFromObect(Object obj)  {
//		Object rtn = JSON.toJSON(obj);
		return JSON.toJSONString(obj);
	}

	public static <T> List<T>  getList(String srcStr){
		List list2 = JSON.parseObject(srcStr,new TypeReference<List<T>>(){});
		return list2;
	}
	
	public static <T> T json2Bean(String jsonStr,Class<T> objClass){
        return JSON.parseObject(jsonStr, objClass);
    }
	
	public static String builderJson() {
		String jsonString = "{";
		jsonString += "\"resphead\": { ";
		jsonString += "\"resultcode\": \"0000\", ";
		jsonString += "\"resultdesc\": \"处理成功！！！\" ";
		jsonString += " }";
		jsonString += "}";
		return jsonString;
	}
	
	public static String builderErrJson(String code,String desc) {
		StringBuilder sb = new StringBuilder();
		sb.append("{"); 
		sb.append("\"resphead\": {");
		sb.append("\"resultcode\": \""+code+"\", ");
		sb.append("\"resultdesc\": \""+desc+"\" ");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	


	
}
