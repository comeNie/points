package com.guohuai.points.res;

import java.util.List;

import com.guohuai.points.entity.PointGoodsEntity;

import lombok.Data;

@Data
public class GoodsRes {

	private List<PointGoodsEntity> rows;
	
	private int page;
	
	private int row;
	
	private int totalPage;
	
	private long total;
}
