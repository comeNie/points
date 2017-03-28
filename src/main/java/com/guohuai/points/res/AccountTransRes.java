package com.guohuai.points.res;

import java.util.List;

import lombok.Data;

import com.guohuai.points.account.entity.AccountTransEntity;

@Data
public class AccountTransRes {

	private List<AccountTransEntity> rows;
	private int page;
	private int row;
	private int totalPage;
	private long total;
}
