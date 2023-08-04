package com.cn.auth.config;

import java.util.Arrays;
import java.util.List;

public class Constant {

	public static final int MAX_WORKER_NUM =20 ;
	public static List<String> subOrgCodeList = Arrays.asList("GZYHG","NSYHG","QZ(A)");

	//超级用户id
	public static final Integer SYSTEM_SUPER_USER = 1;
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
	// ---------token刷新key
	public static final String REDIS_USER_TOKEN_KEY = "TOKEN_KEY";

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
	//五分钟过期
	public static int token_time=5*60;

	public interface taskswitch{
		public static final int open = 9;
		public static final int off = -1;
	};

	public interface is_insurance{
		public static final int yes = 9;
		public static final int no = -1;
	};
	public interface is_tax{
		public static final int yes = 9;
		public static final int no = -1;
	};


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
		public static final String totalconfig_1_2 = "1_2";
		public static final String energy_base_4 = "4";
		public static final String road_level_9 = "9";
		public static final String ship_18 = "18";
		public static final String ship_order_181 = "181";
		public static final String insurance_27 = "27";
		//产品缓存
		public static final String category_88 = "88";
		//工厂缓存
		public static final String factory_101 = "101";
		//运力企业配置
		public static final String transport_team_01 = "01";
		//行业洞察异常影响运费配置
		public static final String industry_freight_config_11_1 = "11_1";
		//行业洞察异常列表
		public static final String industrylist_11 = "11";
		//弱资源车辆配置缓存
		public static final String transport_team_01_1 = "01_1";
		public static final String transport_team_01_2 = "01_2";

		public static final String transport_team_01_3 = "01_3";
		//品类 询价缺省
		public static final String inquiry_99="99";
		//品类 彩信配置
		public static final String category_config_99_1="99_1";
		public static final String totalboxconfig_172 ="17_2" ;
		public static final String road_weather_10 ="10" ;
		public static final String tax_21 ="21" ;
		//询价天数，时间对应关系
		public static final String enery_day_config_9991 ="9991" ;
		public static String wharf_13 ="13" ;

		public static String truck_1 = "truck_1";
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

	public interface give_or_carry {
		public static final int give = 2;
		public static final int carry = 1;
	}
	public interface load {
		public static final int level_A = 0;
		public static final int level_B = 1;
		public static final int level_C = 2;

	}
	public interface Tax {
		public static final int transport = 1;
		public static final int service = 2;

	}

	public  interface block{
		public static final int block_type01 = 1;
		public static final int block_type02 = 2;
		public static final int block_type03 = 3;
		public static final int block_type04 = 4;
	}
	public interface Online {
		//登录方式
		public static final int Log_Type_Phone = 1;
		public static final int Log_Type_WX = 2;

		//未登录
		public static final int Role_Type_NO_Login = 1;
		//未认证
		public static final int Role_Type_Login = 2;
		//个人认证 属于询价人
		public static final int Role_Type_Person = 3;
		//门点 属于询价人
		public static final int Role_Type_Factory = 4;
		//货代 属于询价人
		public static final int Role_Type_Truk = 5;
		//经纪人
		public static final int Role_Type_Agent = 6;
		//个体司机  属于承运人
		public static final int Role_Type_Driver = 7;
		//运力企业  属于承运人
		public static final int Role_Type_Team = 8;

		public interface jika{
			public static final Integer driver=1;
			public static final Integer team=2;
		}
		public interface user{
			public static final String permisson="online_permisson";
		}
		public interface payStatus{
			public static final int sucess=9;
			public static final int initialize=0;
			public static final int fail=-1;
		}
		public interface signStatus{
			//已关注
			public static final int sucess=9;
			//未关注
			public static final int fail=-1;
		}
		public interface News{
			public static final Integer News01=1;
			public static final Integer News02=2;
			public static final Integer News03=3;
			public static final Integer News04=4;
			public static final Integer News05=5;
			public static final Integer News06=6;
			public static final Integer News07=7;
			public static final Integer News08=8;
			public static final Integer News09=9;
			public static final Integer News10=10;
			public static final Integer News11=11;
			public static final Integer News12=12;
			public static final Integer News13=13;
			public static final Integer News14=14;
			public static final Integer News15=15;
			public static final Integer News16=16;
			public static final Integer News17=17;
			public static final Integer News18=18;
			public static final Integer News19=19;
		}


		public interface Jwt{
			public static final String sucess_code="000";
			public static final String sucess_msg="成功";

			public static final String SignatureException_err_code="400";
			public static final String SignatureException_err_msg="JWT签名错误";

			public static final String MalformedJwtException_err_code="401";
			public static final String MalformedJwtException_err_msg="JWT字符串必须包含2个,格式不正确";

			public static final String ExpiredJwtException_err_code="402";
			public static final String ExpiredJwtException_err_msg="JWT已超时";

			public static final String UnsupportedJwtException_err_code="403";
			public static final String UnsupportedJwtException_err_msg="UnsupportedJwtException异常";

			public static final String IllegalArgumentException_err_code="404";
			public static final String IllegalArgumentException_err_msg="IllegalArgumentException异常";

			public static final String Exception_err_code="405";
			public static final String Exception_err_msg="Exception异常";
		}

		public interface Register{
			//审核失败
			public static final int fail=-1;
			//审核成功
			public static final int sucess=9;

			//等待审核
			public static final int wait=1;
			//等待审核被更新
			public static final int wait_delete=-9;
			//未提交认证
			public static final int initial=0;
			//用户撤销审核资料
			public static final int cancel=2;

			public interface image{
				public static final int wait=0;
				public static final int sucess=9;
				public static final int fail=-1;
				//搁置
				public static final int initial=1;
			}
		}


		public interface LoginStatus{
			//-1 禁用  9 未禁用  0注销  1注销申请中  2 注册时候返回被使用用户 8 注销已撤销
			public static final int sucess=9;
			public static final int limit=-1;
			public static final int delete=0;
			public static final int delete_wait=1;
			public static final int cancel_delete_wait=8;
			public static final int resign=2;
		}
		public interface LoginTimeOut{
			// 长期  9  短期 -1
			public static final int long_time=9;
			public static final int short_time=-1;

		}
		public interface sex{
			// 1 男  2 女
			public static final int boy=1;
			public static final int girl=2;

		}
		//小黄象二期前台询价，绕过登录
		public static String fegin_token="PXH0dP5I8qQ8UbFPpzm67cQkm7j8tWT2Kwn6J6SXYkfp2kMolSqHQ==";
		public static String fegin_key="Inner_token";

		public interface side{
			// back  反面 face 正面
			public static final String face="face";
			public static final String back="back";

		}

	}

}
