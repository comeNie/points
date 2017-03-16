package com.guohuai.points.component;

/**
 * @ClassName: TradeType
 * @Description: 交易类别
 * @author xueyunlong
 * @date 2016年11月8日 上午11:28:18
 *
 */
public enum TradeType {
    
	/**
	 * 签到
	 */
	signIn("01"),
	/**
	 * 积分券
	 */
	ticket("02"),
    /**
     * 充值
     */
	recharge("03"),
    /**
     * 消费
     */
	consume("04"),
	;
 
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private TradeType(String value) {
		this.value = value;
	}

}
