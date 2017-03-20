package com.guohuai.points.form;

import lombok.Data;

@Data
public abstract class BaseForm {

	private int page=1;
	
	private int rows=10;
	
}
