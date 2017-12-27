package com.xmj.tool.service.common;  
  
  
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.xmj.demo.XMJMicroServiceApplication;
  
  
/** 
 * Title: ServletInitializer 
 * Description: 相当于整个Web.xml 
 * Company: blog.csdn.net/lu1005287365/ 
 * @author  L lulu 
 * @version 1.0 
 */  
public class ServletInitializer extends SpringBootServletInitializer {  
  
  
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
        return application.sources(XMJMicroServiceApplication.class);  
    }  
  
  
}  
