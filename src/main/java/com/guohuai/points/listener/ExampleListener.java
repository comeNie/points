package com.guohuai.points.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.guohuai.points.component.Constant;
import com.guohuai.points.listener.event.ExampleEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: AuthenticationListener
 * @Description: 鉴权（四要素验证）
 * @author xueyunlong
 * @date 2016年11月8日 下午5:19:02
 *
 */
@Slf4j
@Component
public class ExampleListener {

//	@Autowired
//	private  鉴权接口;
	
	@Value("${integral_environment}")
	String environment;
	
	@EventListener(condition = "#event.tradeType == '01'")
	public void exampleEvent(ExampleEvent event) {
		log.info("An event occured: {}", event);
		String example = event.getExample();
		if("test".equals(environment)){
			example = "测试数据";
		}
		
		/**
		 * 接口功能实现
		 */
		
		event.setReturnCode(Constant.FAIL);
		// 判断是否鉴权成功
		if (null != "") {
			if ("0000".equals("")) {
				event.setReturnCode(Constant.SUCCESS);
			}else{
				event.setErrorDesc("");
				event.setReturnCode(event.getReturnCode());
			}
		}
	}
}
