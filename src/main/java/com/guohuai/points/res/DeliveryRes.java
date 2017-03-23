package com.guohuai.points.res;

import com.guohuai.basic.component.ext.web.BaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeliveryRes extends BaseResp {
	private String oid;
	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * 下单用户名
	 */
	private String userName;
	/**
	 * 收货地址
	 */
	private String address;
	/**
	 * 下单时间
	 */
	private Date orderedTime;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品数量
	 */
	private BigDecimal goodsCount;
	/**
	 * 发货时间
	 */
	private Date sendTime;
	/**
	 * 发货人
	 */
	private String sendOperater;
	/**
	 * 物流公司
	 */
	private String logisticsCompany;
	/**
	 * 物流号
	 */
	private String logisticsNumber;
	/**
	 * 发货状态（0：未发货、1：已发货、2：已取消）
	 */
	private Integer state;
	/**
	 * 取消原因
	 */
	private String cancelReason;
	/**
	 * 取消人
	 */
	private String cancelOperater;
	/**
	 * 取消时间
	 */
	private Date cancelTime;

}
