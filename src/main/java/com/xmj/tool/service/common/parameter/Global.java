package com.xmj.tool.service.common.parameter;

/**
 * 环境变量的常量
 * 
 * @author Val Blake
 *
 */
public class Global {

	// 登陆服务
	public static final String SERVICE_NAME = "boco-poc-login-web";

	// 注册路径
	public static final String REGISTER_URL = "/v1/agent/service/register";

	// 集中配置路径
	public static final String CONFIG_URL = "/v1/ucmp/config/";

	// 服务发现路径
	public static final String SERVICE_FIND_URL = "/v1/agent/service/discoverProdByServiceName";

	// 本地文件的主目录，由管控平台动态指定
	public static final String HOME_FILEDIR = System.getenv("HomeFileDir");

	// 注册中心的服务地址，由管控平台动态指定(http://IP:Port)
	public static final String REGISTRY_ADDRESS = System.getenv("RegistryAddress");

	// 配置中心的服务地址，由管控平台动态指定(http://IP:Port)
	public static final String CFG_ADDRESS = System.getenv("ConfigAddress");

	// 微服务所在宿主机IP地址，由管控平台动态指定
	public static final String SERVICE_IP = System.getenv("ServiceIp");

	// 微服务占用所在宿主机端口，由管控平台动态指定
	public static final String SERVICE_PORT = System.getenv("ServicePort");

	// 微服务的实例ID，由管控平台动态制定。
	public static final String INSTANCE_ID = System.getenv("InstanceID");

	// 微服务运行时的环境标识，由管控平台动态制定。如dev、test等其他环境变量依据应用需求自行定义
	public static final String RUNTIME_ENV = System.getenv("RuntimeEnvID");

	// 分两种，网关和安全中心
	public static final String GET_TOKEN_TYPE = System.getenv("GetTokenType");

	public static final String GET_FROM_APIGW = "APIGateWay";
	public static final String GET_FROM_SC = "SecurityCenter";

	
	public static final String API_GATE_WAY_ADDRESS = System.getenv("ApiGateWayAddress");

	public static final String ASYN_SERVICE_NAME = System.getenv("LoginCallServiceName");
	
	public static final String LOGIN_APPID = System.getenv("LoginAppID");
	
	

	
}
