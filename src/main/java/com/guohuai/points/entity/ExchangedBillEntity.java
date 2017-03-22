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
@Table(name = "t_point_exchanged_bill")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ExchangedBillEntity extends UUID implements Serializable {
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
