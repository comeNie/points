package com.guohuai.points.controller;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.Response;
import com.guohuai.points.form.GoodsForm;
import com.guohuai.points.res.GoodsRes;
import com.guohuai.points.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author mr_gu
 */
@RestController
@RequestMapping(value = "/point/goods")
public class GoodsController {

	private static final String RESULT = "RESULT";
	private static final String SUCCESS = "SUCCESS";

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<Response> save(@Valid GoodsForm req) {
		Response r = new Response();
		//验证参数
		if (StringUtil.isEmpty(req.getName())) {
			r.with(RESULT, "商品名不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getType())) {
			r.with(RESULT, "商品类型不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (null == req.getNeedPoints()) {
			r.with(RESULT, "所需积分不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (null == req.getTotalCount()) {
			r.with(RESULT, "商品数量不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getRemark())) {
			r.with(RESULT, "商品介绍不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getFileOid())) {
			r.with(RESULT, "商品图片不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}

		goodsService.saveGoods(req);
		r.with(RESULT, SUCCESS);

		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<Response> update(@Valid GoodsForm req) {
		Response r = new Response();
		//验证参数
		if (StringUtil.isEmpty(req.getOid())) {
			r.with(RESULT, "无需要修改的商品");
		}
		if (StringUtil.isEmpty(req.getName())) {
			r.with(RESULT, "商品名不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getType())) {
			r.with(RESULT, "商品类型不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (null == req.getNeedPoints()) {
			r.with(RESULT, "所需积分不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (null == req.getTotalCount()) {
			r.with(RESULT, "商品数量不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getRemark())) {
			r.with(RESULT, "商品介绍不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		if (StringUtil.isEmpty(req.getFileOid())) {
			r.with(RESULT, "商品图片不能为空");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}

		goodsService.updateGoods(req);
		r.with(RESULT, SUCCESS);

		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	@RequestMapping(value = "/edit", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ResponseEntity<Response> edit(@Valid GoodsForm req) {
		Response r = new Response();
		//验证参数
		if (StringUtil.isEmpty(req.getOid())) {
			r.with(RESULT, "无需要修改的商品");
			return new ResponseEntity<Response>(r, HttpStatus.OK);
		}
		goodsService.editGoods(req);
		r.with(RESULT, SUCCESS);

		return new ResponseEntity<Response>(r, HttpStatus.OK);
	}

	/**
	 * 获取积分商品列表
	 *
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/page", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	ResponseEntity<GoodsRes> page(GoodsForm form) {
		GoodsRes rows = goodsService.page(form);
		return new ResponseEntity<GoodsRes>(rows, HttpStatus.OK);
	}

}
