package com.wanj.x007_common;

public class CommunicationDefine {
		//#sd 卡保存图片的目录
	public static final String LOCAL_SDCARD_CACHE =  "/sdcard/X007/";
	public static final String WeichatDBName = "x007";


	public static final String RegCode ="x007_kmlm1";
	public static final String Broadcast_WeichatToApp = "x007_xiqncl1";
	public static final String Broadcast_AppToWeichat="x007_xiqnc11";
	public static final String Broadcast_AppToApp="x007_lk9md";


	public  static final  String Broadcast_ACCESSABILITY_Rsp="x007_knd1nd";
	public  static final String Broadcast_ACCESSABILITY_Req="x007_17udn";


	//启动，关闭辅助功能请求
	public  static final String Broadcast_ACCESSABILITY_Bussiness="x007_ln31s";
	//lua 脚本执行
	public  static final String Broadcast_ACCESSABILITY_LUA="x007_a0k1";

	//定位相关
	public  static final String START_LOCATION = "x007_sln1nd";
	public  static final String START_LOCATION_Longtitude = "x007_jna8n1";
	public  static final String START_LOCATION_Latitude = "x007_MNZJ83";

	//全局定位
	public  static final String START_LOCATION_Global = "x007_l01za";
	public  static final String START_LOCATION_Longtitude_Global = "x007_j7za";
	public  static final String START_LOCATION_Latitude_Global = "x007_lmk1z";
	public  static final String START_LOCATION_Address_Global = "x007_1k2m";



	//附近的人,本地配置
	public static final  String Nearby_Sex = "x007_8kdh";
	public static final  String Nearby_AddCount = "x007_jlnzla";
	public static final  String Nearby_AddInterval = "x007_mnjzka";
	public static final  String Nearby_AddIntroduce = "x007_1bnzj";
	public static final  String Nearby_AutoAddName = "x007_nauz";
	public static final  String Nearby_Phone_Filter = "x007_109j3";
	public static final  String Nearby_Add_Request = "x007_m910z";
    public static final String  Last_Location="x007_km10a";
	public static final String  Last_ImportIDPath="x007_mlpz1";
	//采集ID
	public static final  String Nearby_Caiji_Sex = "x007_km1dh";
	public static final  String Nearby_Caiji_Request = "x007_qnh1h";
	//添加ID 好友
	public  static final  String Nearby_ImportID_Count="x007_qina";
	public  static final  String Nearby_ImportID_Intrval="x007_mn108f";
	public  static final  String Nearby_ImportID_Introduce="x007_UHWD";
	public  static final  String Nearby_ImportID_Request="x007_18za";
	//设置
	public  static  final  String APP_Setting="x007_fan12";

	//帐号切换
	public static  final String SwitchAccount_Intrval="x007_1dfa";
	public  static final  String SwitchAccount_Start = "x007_fm1lk";

	//自动回复
	public static  final String AutoReply_OpenTuling="x007_jdk1";
	public static  final String AutoReply_OpenCustomReply="x007_lm10d";
	public static  final String AutoReply_WeichatForground="x007_lmmzd";
	//APP 之间发送的消息标识
	public static  final  int  APP_BroadcastType_StopSwitchAccount=1;
	public static  final  int  APP_BroadcastType_AddTask=2;
	public static  final  int  APP_BroadcastType_GetAccountInfo=3;
	public static  final  int  APP_BroadcastType_AutoChat=4;
	public static final int APP_BroadcastType_StartSwitchAccountReq=5;
	public static final String APP_BroadcastType_StartSwitchAccountRsp="x007_nk1k";
	//点数本地村粗
	public static final String Point_Total="x007_j10az";
	public static final String Point_Use = "x007_jk10da";

	//朋友圈
	public static final String SNS_SendIntrval = "x007_qiyaz";

	//吸粉
	public static final  String AddFriend_AddCount = "x007_pk1zs";
	public static final  String AddFriend_AddInterval = "x007_hy1z";
	public static final  String AddFriend_AddIntroduce = "x007_01zjq";
	public static final  String AddFriend_Add_Request = "x007_p[qaz";
	public static final  String Search_Add_Start="x007_laj1z";
	public static final String Search_Add_Introduce = "x007_mkz10";
	public static final String Search_Add_Name = "x007_lza4";


	//摇一摇
	public static final String Yao_TotalTime="x007_a19qz";
	public static final String Yao_TotalTimeIntrval="x007_im109zma";
	public static final String Yao_CanYao="x007_mk10f";

	//雷达
	public  static final String Radar_TotalTime="x007_76ajz";


	//智能聊天
	public static final String AutoChat_Switch="x007_f1inm";
	public static final String AutoChat_MinIntrval="x007_fl1nm";
	public static final String AutoChat_MaxIntrval="x007_fllnm";
	public static final String AutoChat_TotalTime="x007_flinm";
	public static final String AutoChat_ChatMode="x007_km1hq";
    //点赞
    public static final String DianZan_Start="0x007_1.zjq";

	//个人名片
	public static final String CardInfo_My="x007_uk0am";


	//二维码扫描
	//public static final String  Fake_ScanQRCODE="x007_jnm1s";
	//public static final String  Fake_Orign_QRPath="x007_ln1";
    //二维码加群,加群的一些信息
    public  static final String AddChatRoom_Info = "x007_fn1a";
    public  static final String AddChatRoom_Start = "x007_1ava";

	//通讯录
	public static final  String AddAddress_Sex = "x007_km10h";
	public static final  String AddAddress_AddCount = "x007_zu19f";
	public static final  String AddAddress_AddInterval = "x007_01zyq";
	public static final  String AddAddress_AddIntroduce = "x007_mziba";
    public static final  String AddAddress_AutoDelete = "x007_nzj0d";


	//采集淘宝手机号
	public static final  String Taobao_Count = "x007_kl1;ah";
	public static final  String Taobao_Keyword = "x007_na53";
	public static final  String Taobao_CaijiStart = "x007_jkl1a";

//僵尸好友
    public static final  String Contact_CanDeleteZombile = "x007_09zg";

//搜索微信信息
	public static final  String Search_StartContactInfo = "x007_mkn0a1";
	public static final  String Search_ContactIdentify = "x007_jk1sa";
	public static final  String Search_Count = "x007_nm17z";
	public static final  String Search_Intrval = "x007_nh91da";
}
