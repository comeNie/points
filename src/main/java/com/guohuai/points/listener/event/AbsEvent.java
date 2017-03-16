
package com.guohuai.points.listener.event;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName: AbsEvent
 * @Description:  请求参数抽象.
 * @author xueyunlong
 * @date 2016年11月8日 下午12:29:05
 *
 */
@Data
public abstract class AbsEvent implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 交易类别（接口功能）
	 */
	private String tradeType;
	/**
	 * 返回码 0000 成功 0001失败
	 */
	private String returnCode;
	/**
	 * 错误描述
	 */
	private String errorDesc;

}
