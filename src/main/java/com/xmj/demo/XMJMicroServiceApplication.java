package com.xmj.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.xmj.demo.common.parameter.Global;

@SpringBootApplication
public class XMJMicroServiceApplication extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {
	private static Log log = LogFactory.getFactory().getInstance(XMJMicroServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(XMJMicroServiceApplication.class, args);
		// 注册
		log.info("server started");
//		try {
//			String putMsg = RegisterUtil.register("/healthCheck", null);
//			log.info("register success putMsg[" + putMsg + "]");
//		} catch (Exception e) {
//			log.error("register error", e);
//		}
	}

	/**
	 * 启动端口设定
	 */
	public void customize(ConfigurableEmbeddedServletContainer container) {
		String port = null;
		if(Global.SERVICE_PORT ==null){
			port = "10102";
		}else{
			port = Global.SERVICE_PORT;
		}
		
		container.setPort(Integer.parseInt(port));
	}
	
	
	
}
