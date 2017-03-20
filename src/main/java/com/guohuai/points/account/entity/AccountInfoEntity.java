package com.guohuai.points.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.guohuai.basic.component.ext.hibernate.UUID;


@Entity
@Table(name = "t_point_account")
@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AccountInfoEntity extends UUID implements Serializable {
	
	/**
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = -922483069560508853L;
	
	/**
	 * 账户号
	 */
	private String accountNo;
	/**
	 * 用户ID
	 */
	private String userOid;
	/**
	 * 账户类型：01：积分基本户、02：签到积分户、03：卡券积分户、04：充值积分户
	 */
	private String accountType;
	/**
	 * 账户名称
	 */
	private String accountName;
	/**
	 * 关联卡券
	 */
	private String relationTicketCode;
	/**
	 * 关联卡券名称
	 */
	private String relationTicketName;
	/**
	 * 账户积分
	 */
	private BigDecimal balance;
	/**
	 * NORMAL为正常，FROZEN为冻结
	 */
	private String frozenStatus;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 过期时间
	 */
	private Timestamp overdueTime;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
}
