package com.guohuai.points.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.basic.component.ext.web.Response;
import com.guohuai.points.component.Constant;
import com.guohuai.points.entity.PointSettingEntity;
import com.guohuai.points.form.SettingForm;
import com.guohuai.points.service.SettingService;

/**
 * @author mr_gu
 */
@RestController
@RequestMapping(value = "/points/setting")
public class SettingController {

	@Autowired
	private SettingService settingService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> save(@Valid SettingForm req) {
		Response r = new Response();
		//验证参数
		if(StringUtil.isEmpty(req.getName())) {
			r.with(Constant.RESULT, "积分名不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getPoints()) {
			r.with(Constant.RESULT, "积分数不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getAmount()) {
			r.with(Constant.RESULT, "所需金额不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getTotalCount()) {
			r.with(Constant.RESULT, "总数量不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		
		settingService.savePoints(req);
		r.with(Constant.RESULT, Constant.SUCCESS);

		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> update(@Valid SettingForm req) {
		Response r = new Response();
		//验证参数
		if(StringUtil.isEmpty(req.getOid())){
			r.with(Constant.RESULT, "无需要修改的商品");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(StringUtil.isEmpty(req.getName())) {
			r.with(Constant.RESULT, "积分名不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getPoints()) {
			r.with(Constant.RESULT, "积分数不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getAmount()) {
			r.with(Constant.RESULT, "所需金额不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if(null == req.getTotalCount()) {
			r.with(Constant.RESULT, "总数量不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}

		settingService.updatePoints(req);
		r.with(Constant.RESULT, Constant.SUCCESS);

		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	@RequestMapping(value = "/edit", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<BaseResp> edit(@Valid SettingForm req) {
		BaseResp repponse = new BaseResp();
		//验证参数
		if(StringUtil.isEmpty(req.getOid())){
			repponse.setErrorCode(-1);
			repponse.setErrorMessage("无需要修改的商品");
			return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
		}
		
		repponse = settingService.editPoints(req);
    	return new ResponseEntity<BaseResp>(repponse, HttpStatus.OK);
	}

	/**
	 * 获取积分产品列表
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/page", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	ResponseEntity<PageResp<PointSettingEntity>> page(SettingForm form) {
		PageResp<PointSettingEntity> rows = settingService.page(form);
		return new ResponseEntity<PageResp<PointSettingEntity>>(rows, HttpStatus.OK);
	}

}
