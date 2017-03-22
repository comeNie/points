package com.guohuai.points.res;

import com.guohuai.basic.component.ext.web.BaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ExchangedBillRes extends BaseResp {
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

}
