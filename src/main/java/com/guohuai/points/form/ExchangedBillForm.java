package com.guohuai.points.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ExchangedBillForm extends BaseForm {
	/**
	 * 积分商品Id
	 */
	private String goodsOid;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品类型（枚举: real实物、virtual虚拟）
	 */
	private String type;
	/**
	 * 用户
	 */
	private String userOid;
	/**
	 * 兑换数量
	 */
	private BigDecimal exchangedCount;
	/**
	 * 兑换时间
	 */
	private Date exchangedTime;
	/**
	 * 状态：0成功、1：失败
	 */
	private Integer state;
	// 用于搜索（开始时间）
	private Date startTime;
	// 用于搜索（结束时间）
	private Date endTime;
}
