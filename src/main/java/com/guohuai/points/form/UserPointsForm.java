package com.guohuai.points.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserPointsForm extends BaseForm {
	private String oid;
	/**
	 * 会员id
	 */
	private String userOid;
	/**
	 * 01：签到，02：卡券，03：充值，04：消费，05：过期， 06：撤单
	 */
	private String orderType;
	/**
	 * 账户类型
	 */
	public String accountType;
	/**
	 * 关联产品、卡券编号
	 */
	private String relationProductNo;
	/**
	 * 关联产品、卡券名称
	 */
	private String relationProductName;
	/**
	 * 交易积分额
	 */
	private BigDecimal orderPoint;
	/**
	 * 交易后积分余额
	 */
	private BigDecimal balance;
	/**
	 * 备注
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
	 * 交易账户
	 */
	private String transAccountNo;
	/**
	 * 积分方向，增ADD 减REDUCE
	 */
	private String direction;
	/**
	 * 最小交易积分
	 */
	private BigDecimal minOrderPoint;
	/**
	 * 最大交易积分
	 */
	private BigDecimal maxOrderPoint;
	// 用于搜索（开始时间）
	private String beginTime;
	// 用于搜索（结束时间）
	private String endTime;
}
