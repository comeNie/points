package com.guohuai.points.listener.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ExampleEvent
 * @Description: 例子事件
 * @author chendonghui
 * @date 2017年3月16日 下午3:01:19
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ExampleEvent extends AbsEvent{
	private static final long serialVersionUID = 6047514142746460173L;
	 /**
	  * 例子
	 */
	private String example;
	
}
