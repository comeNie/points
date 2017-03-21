package com.guohuai.points.res;

import java.io.Serializable;

import lombok.Data;

@Data
public  class BaseResponse implements Serializable {

	private static final long serialVersionUID = -1748346599148347108L;
	private String returnCode;
	private String errorMessage;
}
