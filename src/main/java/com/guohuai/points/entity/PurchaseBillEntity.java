package com.guohuai.points.entity;

import com.guohuai.basic.component.ext.hibernate.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "t_point_purchase_bill")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PurchaseBillEntity extends UUID implements Serializable {
	/**
	 * 用户ID
	 */
	private String userOid;
	/**
	 * 积分产品id
	 */
	private String settingOid;
	/**
	 * 购买的积分
	 */
	private BigDecimal purchasePoints;
	/**
	 * 花费的金额
	 */
	private BigDecimal amount;
	/**
	 * 购买时间
	 */
	private Date purchaseTime;
	/**
	 * 状态：0:成功、1:失败
	 */
	private Integer state;

	private Date createTime;
	private Date updateTime;
}
