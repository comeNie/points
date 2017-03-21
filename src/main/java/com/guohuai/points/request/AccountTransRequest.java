package com.guohuai.points.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 账户交易请求参数
* @ClassName: NewUserRequest 
* @Description: 
* @author longyunbo
* @date 2016年11月8日 上午10:10:41 
*
 */
@Data
public class AccountTransRequest implements Serializable{
	/**
	* @Fields serialVersionUID : 
	*/
	private static final long serialVersionUID = 2136893610065389423L;
	/**
	 * 会员id
	 */
	private String userOid; 
	/**
	 * 用户类型
	 * 投资人账户:T1、发行人账户:T2、平台账户:T3  
	 */
	private String userType; 
	/**
	 * 单据类型
	 * 申购:01、赎回:02、派息:03、赠送体验金:04、体验金到期:05
	 */
	private String orderType; 
	
	/**
	 * 产品类别 活期  01、定期 06
	 * 在申购 赎回 增加发行人份额记账 派系时需要传入 
	 */
	private String productType;
	/**
	 * 关联产品编号
	 */
	private String relationProductNo; 
	/**
	 * 关联产品名称
	 */
	private String relationProductName; 
	/**
	 * 交易额
	 */
	private BigDecimal balance;	
	/**
	 * 代金券
	 */
	private BigDecimal voucher;
	/**
	 * 交易用途
	 */
	private String remark; 
	/**
	 * 来源系统单据号
	 */
	private String orderNo; 
	/**
	 * 来源系统类型
	 */
	private String systemSource; 
	/**
	 * 请求流水号
	 */
	private String requestNo;
	/**
	 * 定单描述
	 */
	private String orderDesc;
	
	/**
	 * 转出产品编号
	 */
	private String outputRelationProductNo;
	
	/**
	 * 转出产品名称
	 */
	private String outputRelationProductName;
	
	/**
	 * 业务系统订单创建时间
	 */
	private String orderCreatTime;
	
}
