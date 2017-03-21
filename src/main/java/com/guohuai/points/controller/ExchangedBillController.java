package com.guohuai.points.controller;

import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.basic.component.ext.web.PageResp;
import com.guohuai.points.form.ExchangedBillForm;
import com.guohuai.points.res.ExchangedBillRes;
import com.guohuai.points.service.ExchangedBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分记录
 */
@RestController
@RequestMapping(value = "/points/exchangedBill/")
@Slf4j
public class ExchangedBillController {

	@Autowired
	ExchangedBillService exchangedBillService;

	@RequestMapping(value = "list")
	@ResponseBody
	public ResponseEntity<PageResp<ExchangedBillRes>> page(ExchangedBillForm req) {

		PageResp<ExchangedBillRes> pageResp = new PageResp<>();

		if (StringUtil.isEmpty(req.getUserOid())) {
			pageResp.setErrorCode(-1);
			pageResp.setErrorMessage("用户ID为空！");
			return new ResponseEntity<PageResp<ExchangedBillRes>>(pageResp, HttpStatus.OK);
		}
		log.info("PurchaseBillController分页查询用户ID：", req.getUserOid());

		pageResp = exchangedBillService.page(req);
		return new ResponseEntity<PageResp<ExchangedBillRes>>(pageResp, HttpStatus.OK);
	}


	@RequestMapping(value = "findById")
	@ResponseBody
	public ResponseEntity<BaseResp> findById(String oid) {

		if (StringUtil.isEmpty(oid)) {
			BaseResp baseResp = new BaseResp(-1, "id为空！");
			return new ResponseEntity<BaseResp>(baseResp, HttpStatus.OK);
		}
		log.info("查询单条记录ID：", oid);
		ExchangedBillRes billRes = exchangedBillService.findById(oid);

		return new ResponseEntity<BaseResp>(billRes, HttpStatus.OK);
	}
}

