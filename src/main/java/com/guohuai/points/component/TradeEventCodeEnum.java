package com.guohuai.points.component;

public enum TradeEventCodeEnum {
	
	trade_1001("1001","定单来源不能为空"),
	trade_1002("1002","定单类别不能为空"),
	trade_1003("1003","定单金额不能为空"),
	trade_1004("1004","末匹配到支付渠道"),
	trade_1005("1005","定单类别不支持"),
	trade_1006("1006","请绑定银行卡"),
	trade_1007("1007","大额赎回"),
	trade_1008("1008","绑定银行卡交易额度超过日限额"),
	trade_1009("1009","不支持该银行卡"),
	trade_1010("1010","绑定银行卡交易额度超过单笔限额"),
	;
	
	private String code;
	private String name;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private TradeEventCodeEnum(String code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static String getEnumName(final String value) {
		for (TradeEventCodeEnum tradeEnum : TradeEventCodeEnum.values()) {
			if (tradeEnum.getCode().equals(value)) {
				return tradeEnum.getName();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.code;
	}
}
