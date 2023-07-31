package com.cn.auth.config;


import java.util.Arrays;
import java.util.List;

public class Constant {

    public static final int MAX_WORKER_NUM =20 ;
    public static List<String> subOrgCodeList = Arrays.asList("GZYHG","NSYHG","QZ(A)");

	//超级用户id
	public static final Long SYSTEM_SUPER_USER = 1L;
	//角色常量
	//司机角色编码
	public static final String DRIVER_ROLE_CODE = "DRIVER";
	//客户角色编码
	public static final String CUSTOMER_ROLE_CODE = "CUSTOMER";

	//账户密码半个小时最多错误5次
	public static final String ERRO_PASSWORD = "ERRO_PASSWORD";
	//根据key值服务端接口调用免验证权限
	public static final String EIPSERVICE_KEY_NAMME = "EIPSERVICE_KEY";
	public static final String EIPSERVICE_KEY_VALUE = "965dd51ffd37365609cc1fedded9948d";

	// ------------ 管理用户Session
	public static final String MANAGE_SESSION_USER = "MANAGE_SESSION_USER";
	public static final String MANAGE_USER_PERMISSION = "MANAGE_USER_PERMISSION";
	public static final String USER_MENU_FILE = "USER_MENU_FILE";
	public static final String REDIS_USER_CACHE_KEY = "userCache";
    public static final String REDIS_USER_CACHE_ID_ = "user_id_";

	public static final String REDIS_ISSUED_USER_CACHE_KEY = "issuedUserCache";
	public static final String REDIS_ISSUED_USER_CACHE_ID_ = "issued_user_id_";

    public static final String REDIS_PERMISSION_CACHE_KEY = "permissionCache";
    public static final String REDIS_PERMISSION_CACHE_ID_ = "permission_id_";
    public static final String REDIS_PERMISSION_CACHE_ID_ASSESSMENT = "assessment_permission_id_";
	public static final String REDIS_XM_USER_TOKEN_KEY = "xmUserTokenCache";
	public static final String REDIS_XM_USER_NAME = "xm_user_name_ygb";
	// ------------ 当前登录用户所属部门编码
	public static final String MANAGE_REQUEST_DEPART_CODE = "MANAGE_REQUEST_DEPART_CODE";

	// ------------ 当前登录用户id
	public static final String MANAGE_REQUEST_USER_ID = "MANAGE_REQUSET_USER_ID";
	
	public static final String MANAGE_REQ_MENUURL = "MANAGE_REQ_MENUURL";

	
	public static final int DEPTMENT_LEVEL_TOP1 = 1;
	
	public static final int DEPTMENT_LEVEL_TOP2 = 2;

	public static final int DEPTMENT_LEVEL_TOP3 = 3;

	// ------------ 管理用户状态
	
	public static final int MUSER_STATUS_YES = 1;
	//被禁用
	public static final int MUSER_STATUS_NO = 0;



	// ------------ 错误码定义
	
	public static final String ERROR_CODE_4011 = "4011";
	
	public static final String ERROR_CODE_5000 = "5000";
	
	public static final String ERROR_CODE_5001 = "5001";


	// ------------ 接口数据来源
	
	public static final int WS_SOURCE_BASIC = 0;
	
	public static final int WS_SOURCE_FY = 1;
	
	public static final int WS_SOURCE_AT = 2;


	/**
	 * 放置redis中key对应的java枚举,防止key重复
	 *
	 * 命名规范：类型_模块_XXX_XXX_XXX, 建议最多5段
	 *
	 * 类型取值：键值对=k，列表=l,map=h,队列=q,hashSet=s,经过排序后的hashset=z
	 *
	 */
	public static  final String K_AUTH_CODE = "k_auth_code_";

	/**
	 * 甘永恒
	 */
	public static final String XHX_CACHE_MQ_KEY = "xhx_cache_mq_key";




	//'折旧方式  1：等额法  2：递减法'
	public static Integer depreciation_type_1=1;
	public static Integer depreciation_type_2=2;

	public interface VehicleType {
		public static final int oli = 1;
		public static final int electric = 2;
		public static final int mix = 3;
		//品类缺省配置
		public static final String inquiry_99="99";
	}
	public interface CityPost {
		//南方，中石化
		public static final int south = 1;
		//北方 ,代表中石油
		public static final int north = 2;
	}
	//1 经纪人报价  2 询价人  3 承运人
	public interface IsAgent {
		public static final int yes = 1;
		public static final int no = 2;
		public static final int driver = 3;
	}

	public interface status {
		//上线
		public static final int online = 9;
		//下线
		public static final int offline = -1;
	}
	public interface Insurance_type {
		//按货物计算
		public static final int rate = 1;
		//一口价
		public static final int fee = 2;
	}
	public interface ship_config_type {
		//按货物计算
		public static final int inRoom = 1;
		//一口价
		public static final int outRoom = 2;
	}

	public interface mq_cache_key {
		public static final String totalconfig_1 = "1";
		//刷新流域缓存配置
		public static final String totalconfig_1_1 = "1_1";
		public static final String energy_base_4 = "4";
		public static final String road_level_9 = "9";
		public static final String ship_18 = "18";
		public static final String ship_order_181 = "181";
		public static final String insurance_27 = "27";
		//产品缓存
		public static final String category_88 = "88";
		//运力企业配置
		public static final String transport_team_01 = "01";
		//弱资源车辆配置缓存
		public static final String transport_team_01_1 = "01_1";
		public static final String transport_team_01_2 = "01_2";

		public static final String transport_team_01_3 = "01_3";
		public static final String inquiry_99="99";
		public static final String totalboxconfig_172 ="17_2" ;
		public static final String road_weather_10 ="10" ;
    }


    //流域配置
	public interface basin {
		public static final Integer company_manage_fee_type_0 = 0;
		public static final Integer company_manage_fee_type_1 = 1;
		public static final Integer company_manage_fee_type_2 = 2;
	}


	//是否双柜 1 双  0 单
	public interface DoubleCabinet {
		public static final int yes = 1;
		public static final int no = 0;
	}
	public interface box_size {
		public static final int size_20 = 20;
		public static final int size_40 = 40;
		public static final int size_45 = 45;
	}
}
