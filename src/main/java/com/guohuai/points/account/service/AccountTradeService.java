package com.guohuai.points.account.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.guohuai.basic.common.StringUtil;
import com.guohuai.basic.component.ext.web.BaseResp;
import com.guohuai.points.account.entity.AccOrderEntity;
import com.guohuai.points.account.entity.AccountInfoEntity;
import com.guohuai.points.account.entity.AccountTransEntity;
import com.guohuai.points.component.AccountTypeEnum;
import com.guohuai.points.component.Constant;
import com.guohuai.points.component.TradeEventCodeEnum;
import com.guohuai.points.component.TradeType;
import com.guohuai.points.request.AccountTradeRequest;
import com.guohuai.points.request.AccountTransRequest;
import com.guohuai.points.request.CreateAccOrderRequest;
import com.guohuai.points.request.CreateAccountRequest;
import com.guohuai.points.res.AccountTradeResponse;
import com.guohuai.points.res.AccountTransResponse;
import com.guohuai.points.res.CreateAccOrderResponse;
import com.guohuai.points.res.CreateAccountResponse;

/**
 * @ClassName: AccountTradeService
 * @Description: 积分交易相关
 * @author CHENDONGHUI
 * @date 2017年3月21日 下午6:11:12
 */
@Service
public class AccountTradeService {
	private final static Logger log = LoggerFactory.getLogger(AccountTradeService.class);
	
	@Autowired
	private AccountInfoService accountInfoService;
	
	@Autowired
	private AccOrderService accOrderService;
	
	@Autowired
	private AccountTransService accountTransService;
	
	/**
	 * 积分交易
	 * @param accountTradeRequest
	 * @return
	 */
	@Transactional
	public AccountTradeResponse trade(AccountTradeRequest accountTradeRequest){
		log.info("积分账户交易：AccountTradeRequest{}",JSONObject.toJSONString(accountTradeRequest));
		AccountTradeResponse accountTradeResponse = new AccountTradeResponse();
		accountTradeResponse.setRequestNo(accountTradeRequest.getRequestNo());
		accountTradeResponse.setUserOid(accountTradeRequest.getUserOid());
		String orderStatus = Constant.PAY2;//交易失败
		//系统来源
		if (StringUtil.isEmpty(accountTradeRequest.getSystemSource())) {
			accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2001.getCode());
			accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2001.getName());
			log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
			return accountTradeResponse;
		}
		//订单类型
		if (StringUtil.isEmpty(accountTradeRequest.getOrderType())) {
			accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2002.getCode());
			accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2002.getName());
			log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
			return accountTradeResponse;
		}
		//会员id
		if (StringUtil.isEmpty(accountTradeRequest.getUserOid())) {
			accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2003.getCode());
			accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2003.getName());
			log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
			return accountTradeResponse;
		}
		//当订单为撤单，需传原订单
		if (StringUtil.isEmpty(accountTradeRequest.getOldOrderNo()) && TradeType.KILLORDER.equals(accountTradeRequest.getOrderType())) {
			accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2009.getCode());
			accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2009.getName());
			log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
			return accountTradeResponse;
		}
		//第一步，接收订单，创建订单
		log.info("");
		CreateAccOrderRequest createAccOrderRequest = new CreateAccOrderRequest();
		//组装创建基恩账户入参
		createAccOrderRequest.setOrderNo(accountTradeRequest.getOrderNo());
		createAccOrderRequest.setRequestNo(accountTradeRequest.getRequestNo());
		createAccOrderRequest.setSystemSource(accountTradeRequest.getSystemSource());
		createAccOrderRequest.setUserOid(accountTradeRequest.getUserOid());
		createAccOrderRequest.setOrderType(accountTradeRequest.getOrderType());
		createAccOrderRequest.setRelationProductNo(accountTradeRequest.getRelationProductNo());
		createAccOrderRequest.setRelationProductName(accountTradeRequest.getRelationProductName());
		createAccOrderRequest.setBalance(accountTradeRequest.getBalance());
		createAccOrderRequest.setRemark(accountTradeRequest.getRemark());
		createAccOrderRequest.setOrderDesc(accountTradeRequest.getOrderDesc());
		createAccOrderRequest.setOldOrderNo(accountTradeRequest.getOldOrderNo());//原订单号
		CreateAccOrderResponse createAccOrderResponse = new CreateAccOrderResponse();
		try{
			createAccOrderResponse = accOrderService.addAccOrder(createAccOrderRequest);
			if(!Constant.SUCCESS.equals(createAccOrderResponse.getReturnCode())){//判断是否创建订单成功
				accountTradeResponse.setReturnCode(createAccOrderResponse.getReturnCode());
				accountTradeResponse.setErrorMessage(createAccOrderResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
		}catch(Exception e){
			log.info("积分账户创建订单失败："+e);
			accountTradeResponse.setReturnCode(Constant.FAIL);
			accountTradeResponse.setErrorMessage("系统异常");
			log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
			return accountTradeResponse;
		}
		//根据订单类型进行不同处理
		if(TradeType.SIGNIN.getValue().equals(accountTradeRequest.getOrderType())
				||TradeType.TICKET.getValue().equals(accountTradeRequest.getOrderType())
				||TradeType.RECHARGE.getValue().equals(accountTradeRequest.getOrderType())){//签到||卡券||充值
			//过期时间不能为空
			if(accountTradeRequest.getOverdueTime() == null){
				accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2008.getCode());
				accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2008.getName());
				callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
			//第二步，创建积分基本户（不存在时创建，已存在不创建）
			CreateAccountRequest createAccountRequest = new CreateAccountRequest();
			//组装基本户参数
			createAccountRequest.setAccountType(AccountTypeEnum.ACCOUNT_TYPE01.getCode());//积分基本户
			createAccountRequest.setRemark(accountTradeRequest.getRemark());
			createAccountRequest.setUserOid(accountTradeRequest.getUserOid());
			AccountInfoEntity accountInfoEntity = accountInfoService.addBaseAccount(createAccountRequest);
			//第三步，创建对应子账户
			String accountType = "";//根据订单类型判断积分子账户类型
			//组装子账户参数
			if(TradeType.SIGNIN.getValue().equals(accountTradeRequest.getOrderType())){
				accountType = AccountTypeEnum.ACCOUNT_TYPE02.getCode();
			}else if(TradeType.TICKET.getValue().equals(accountTradeRequest.getOrderType())){
				accountType = AccountTypeEnum.ACCOUNT_TYPE03.getCode();
			}else if(TradeType.RECHARGE.getValue().equals(accountTradeRequest.getOrderType())){
				accountType = AccountTypeEnum.ACCOUNT_TYPE04.getCode();
			}else{//订单类型不支持
				accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2005.getCode());
				accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2005.getName());
				callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
			createAccountRequest.setAccountType(accountType);
			createAccountRequest.setRelationProduct(accountTradeRequest.getRelationProductNo());
			createAccountRequest.setRelationProductName(accountTradeRequest.getRelationProductName());
			createAccountRequest.setOverdueTime(accountTradeRequest.getOverdueTime());
			CreateAccountResponse createAccountResponse = accountInfoService.addChildAccount(createAccountRequest);
			if(Constant.SUCCESS.equals(createAccountResponse.getReturnCode())){//判断子积分账户创建结果
				//第三步，记录积分账户交易流水并对相应账户进行操作
				List<Map<String, Object>> accountInfoList = new ArrayList<Map<String, Object>>();//执行操作账户List
				List<AccountTransRequest> accounttransList = new ArrayList<AccountTransRequest>();//记录交易流水List
				
				//子账户操作
				Map<String, Object> map1 = new HashMap<String, Object>();
				BigDecimal childbalance = add(createAccountResponse.getBalance(),accountTradeRequest.getBalance());//子账户积分余额+交易积分
				log.info("{}用户积分子帐户{}积分增加：{}",accountTradeRequest.getUserOid(),createAccountResponse.getAccountNo(),childbalance);
				//组装操作账户入参
				map1.put("oid", createAccountResponse.getOid());
				map1.put("balance", childbalance);
				accountInfoList.add(map1);
				
				AccountTransRequest accountTransRequest1 = installAccountTransRequest(accountTradeRequest, createAccountResponse.getAccountNo(), accountType, childbalance);
				//子账户
				accounttransList.add(accountTransRequest1);
				
				//基本户操作
				Map<String, Object> map2 = new HashMap<String, Object>();
				BigDecimal basicbalance = add(accountInfoEntity.getBalance(),accountTradeRequest.getBalance());//基本户积分余额+交易积分
				log.info("{}用户积分基本帐户{}积分增加：{}",accountTradeRequest.getUserOid(),accountInfoEntity.getAccountNo(),basicbalance);
				map2.put("oid", accountInfoEntity.getOid());
				map2.put("balance", basicbalance);
				accountInfoList.add(map2);
				
				AccountTransRequest accountTransRequest2 = installAccountTransRequest(accountTradeRequest, accountInfoEntity.getAccountNo(), AccountTypeEnum.ACCOUNT_TYPE01.getCode(), basicbalance);
				//基本户
				accounttransList.add(accountTransRequest2);
				
				//记录积分账户交易流水
				if(accounttransList != null && accounttransList.size()>0){
					AccountTransResponse accountTransResponse = accountTransService.addAccTransList(accounttransList);
					if(!Constant.SUCCESS.equals(accountTransResponse.getReturnCode())){
						accountTradeResponse.setReturnCode(Constant.FAIL);
						accountTradeResponse.setErrorMessage(accountTransResponse.getErrorMessage());
						callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
						log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
						return accountTradeResponse;
					}
				}
				//对相应账户进行操作
				if(accountInfoList != null && accountInfoList.size()>0){
					BaseResp baseResp = accountInfoService.update(accountInfoList);
					if(baseResp.getErrorCode()==0){
						orderStatus = Constant.PAY1;//交易成功
					}else{
						accountTradeResponse.setReturnCode(Constant.FAIL);
						accountTradeResponse.setErrorMessage(baseResp.getErrorMessage());
						callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
						return accountTradeResponse;
					}
				}
			}else{
				accountTradeResponse.setReturnCode(createAccOrderResponse.getReturnCode());
				accountTradeResponse.setErrorMessage(createAccOrderResponse.getErrorMessage());
				callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
		}else if(TradeType.CONSUME.getValue().equals(accountTradeRequest.getOrderType())){//消费积分
			//第一步，查询基本户余额，判断是否够消费
			AccountInfoEntity basicAccountEntity = accountInfoService.getAccountByTypeAndUser(AccountTypeEnum.ACCOUNT_TYPE01.getCode(), accountTradeRequest.getUserOid());
			if(basicAccountEntity != null){
				BigDecimal basicbalance = basicAccountEntity.getBalance();//基本户积分总额
				BigDecimal orderBalance = accountTradeRequest.getBalance();//积分交易额
				//判断是否够消费
				if(isExcess(basicbalance, orderBalance)){
					List<Map<String, Object>> accountInfoList = new ArrayList<Map<String, Object>>();//执行操作账户List
					List<AccountTransRequest> accounttransList = new ArrayList<AccountTransRequest>();//记录交易流水List
					
					//组装交易流水入参
					AccountTransRequest accountTransRequest = new AccountTransRequest();
					accountTransRequest.setDirection("reduce");
					accountTransRequest.setOrderDesc(accountTradeRequest.getOrderDesc());
					accountTransRequest.setOrderNo(accountTradeRequest.getOrderNo());
					accountTransRequest.setOrderType(accountTradeRequest.getOrderType());
					accountTransRequest.setRelationProductName(accountTradeRequest.getRelationProductName());
					accountTransRequest.setRelationProductNo(accountTradeRequest.getRelationProductNo());
					accountTransRequest.setRemark(accountTradeRequest.getRemark());
					accountTradeRequest.setRequestNo(accountTradeRequest.getRequestNo());
					accountTradeRequest.setSystemSource(accountTradeRequest.getSystemSource());
					accountTransRequest.setUserOid(accountTradeRequest.getUserOid());
					
					//基本户操作
					Map<String, Object> map2 = new HashMap<String, Object>();
					basicbalance = sub(basicbalance, orderBalance);//积分基本户扣除相应的积分
					log.info("{}用户积分基本帐户{}积分扣除：{}",accountTradeRequest.getUserOid(), basicAccountEntity.getAccountNo(), basicbalance);
					map2.put("oid", basicAccountEntity.getOid());
					map2.put("balance", basicbalance);
					accountInfoList.add(map2);
					
					accountTransRequest.setTransAccountNo(basicAccountEntity.getAccountNo());
					accountTransRequest.setAccountType(AccountTypeEnum.ACCOUNT_TYPE01.getCode());
					accountTransRequest.setBalance(basicbalance);
					accountTransRequest.setOrderPoint(orderBalance);
					//基本户
					accounttransList.add(accountTransRequest);
					//第二步，对用户名下所有积分账户进行操作
					//查询该用户名下所有积分子账户
					List<AccountInfoEntity> childAccountInfoList = accountInfoService.getChildAccountListByUser(accountTradeRequest.getUserOid());
					for(AccountInfoEntity accountInfoEntity : childAccountInfoList){
						BigDecimal childbalance = basicAccountEntity.getBalance();
						//子账户操作
						Map<String, Object> map1 = new HashMap<String, Object>();
						if(isExcess(childbalance, orderBalance)){//判断子账户是否够
							childbalance = sub(childbalance, orderBalance);//积分子帐户扣除相应的积分
							accountTransRequest.setTransAccountNo(accountInfoEntity.getAccountNo());
							accountTransRequest.setAccountType(accountInfoEntity.getAccountType());
							accountTransRequest.setBalance(childbalance);
							accountTransRequest.setOrderPoint(orderBalance);
							//子账户
							accounttransList.add(accountTransRequest);
							
							log.info("{}用户积分子帐户{}积分扣除：{}，余额：{}",accountTradeRequest.getUserOid(),accountInfoEntity.getAccountNo(),orderBalance,childbalance);
							//组装操作账户入参
							map1.put("oid", accountInfoEntity.getOid());
							map1.put("balance", childbalance);
							accountInfoList.add(map1);
							break;
						}else{
							accountTransRequest.setTransAccountNo(accountInfoEntity.getAccountNo());
							accountTransRequest.setAccountType(accountInfoEntity.getAccountType());
							accountTransRequest.setBalance(BigDecimal.ZERO);//余额为0
							accountTransRequest.setOrderPoint(childbalance);//订单积分为原可用积分
							//子账户
							accounttransList.add(accountTransRequest);
							orderBalance = sub(orderBalance, childbalance);//订单积分额减去已扣除积分额
							log.info("{}用户积分子帐户{}积分扣除：{}，积分余额：{}，剩余需扣积分{}",
									accountTradeRequest.getUserOid(),accountInfoEntity.getAccountNo(),childbalance,BigDecimal.ZERO,orderBalance);
							//组装操作账户入参
							map1.put("oid", accountInfoEntity.getOid());
							map1.put("balance", childbalance);
							accountInfoList.add(map1);
						}
					}
					//第三步，记录积分账户交易流水并对相应账户进行操作
					//记录积分账户交易流水
					if(accounttransList != null && accounttransList.size()>0){
						AccountTransResponse accountTransResponse = accountTransService.addAccTransList(accounttransList);
						if(!"Constant.SUCCESS".equals(accountTransResponse.getReturnCode())){
							accountTradeResponse.setReturnCode(Constant.FAIL);
							accountTradeResponse.setErrorMessage(accountTransResponse.getErrorMessage());
							callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
							log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
							return accountTradeResponse;
						}
					}
					//对相应账户进行操作
					if(accountInfoList != null && accountInfoList.size()>0){
						BaseResp baseResp = accountInfoService.update(accountInfoList);
						if(baseResp.getErrorCode()==0){
							orderStatus = Constant.PAY1;//交易成功
						}else{
							accountTradeResponse.setReturnCode(Constant.FAIL);
							accountTradeResponse.setErrorMessage(baseResp.getErrorMessage());
							callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
							log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
							return accountTradeResponse;
						}
					}
				}else{//积分消费超额
					accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2007.getCode());
					accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2007.getName());
					//回写订单状态
					callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
					log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
					return accountTradeResponse;
				}
			}else{//积分账户不存在，按积分为0提示，消费不创建积分账户
				accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2006.getCode());
				accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2006.getName());
				//回写订单状态
				callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
		}else if(TradeType.KILLORDER.getValue().equals(accountTradeRequest.getOrderType())){//撤单
			//第一步，查询原订单是否存在
			AccOrderEntity oldOrderEntity = accOrderService.getOrderByNo(accountTradeRequest.getOldOrderNo());
			if(oldOrderEntity != null){
				if(Constant.PAY1.equals(oldOrderEntity.getOrderStatus())){
					if(TradeType.CONSUME.equals(oldOrderEntity.getOrderType())){
						//第二步，查询该订单所有交易流水
						List<AccountTransEntity> oldAccountTransList = accountTransService.getUserAccountTransByOrderNo
								(accountTradeRequest.getUserOid(), accountTradeRequest.getOldOrderNo());
						if(oldAccountTransList != null && oldAccountTransList.size()>0){
							//第三步，根据流水回退操作账户积分，并记录交易流水
							List<Map<String, Object>> accountInfoList = new ArrayList<Map<String, Object>>();//执行操作账户List
							List<AccountTransRequest> accounttransList = new ArrayList<AccountTransRequest>();//记录交易流水List
							//组装交易流水入参
							AccountTransRequest accountTransRequest = new AccountTransRequest();
							accountTransRequest.setDirection("add");
							accountTransRequest.setRelationProductName(oldOrderEntity.getRelationProductName());
							accountTransRequest.setRelationProductNo(oldOrderEntity.getRelationProductCode());
							accountTransRequest.setOrderDesc(accountTradeRequest.getOrderDesc());
							accountTransRequest.setOrderNo(accountTradeRequest.getOrderNo());
							accountTransRequest.setOrderType(accountTradeRequest.getOrderType());
							accountTransRequest.setRemark(accountTradeRequest.getRemark());
							accountTradeRequest.setRequestNo(accountTradeRequest.getRequestNo());
							accountTradeRequest.setSystemSource(accountTradeRequest.getSystemSource());
							accountTransRequest.setUserOid(accountTradeRequest.getUserOid());
							
							for(AccountTransEntity oldAccountTransEntity : oldAccountTransList){
								Map<String, Object> map = new HashMap<String, Object>();
								//查询该账户积分余额
								AccountInfoEntity accountInfoEntity = accountInfoService.getAccountByNo(oldAccountTransEntity.getTransAccountNo());
								BigDecimal balance = add(oldAccountTransEntity.getOrderPoint(), accountInfoEntity.getBalance());//积分子帐户增加相应的积分
								accountTransRequest.setTransAccountNo(oldAccountTransEntity.getTransAccountNo());
								accountTransRequest.setAccountType(oldAccountTransEntity.getAccountType());
								accountTransRequest.setBalance(balance);
								accountTransRequest.setOrderPoint(oldAccountTransEntity.getOrderPoint());
								//相应账户
								accounttransList.add(accountTransRequest);
								
								log.info("{}用户积分帐户{}积分增加：{}，余额：{}",accountTradeRequest.getUserOid(),oldAccountTransEntity.getTransAccountNo(),oldAccountTransEntity.getOrderPoint(),balance);
								//组装操作账户入参
								map.put("oid", accountInfoEntity.getOid());
								map.put("balance", balance);
								accountInfoList.add(map);
							}
							//记录积分账户交易流水
							if(accounttransList != null && accounttransList.size()>0){
								AccountTransResponse accountTransResponse = accountTransService.addAccTransList(accounttransList);
								if(!"Constant.SUCCESS".equals(accountTransResponse.getReturnCode())){
									accountTradeResponse.setReturnCode(Constant.FAIL);
									accountTradeResponse.setErrorMessage(accountTransResponse.getErrorMessage());
									callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
									log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
									return accountTradeResponse;
								}
							}
							//对相应账户进行操作
							if(accountInfoList != null && accountInfoList.size()>0){
								BaseResp baseResp = accountInfoService.update(accountInfoList);
								if(baseResp.getErrorCode()==0){
									orderStatus = Constant.PAY1;//交易成功
								}else{
									accountTradeResponse.setReturnCode(Constant.FAIL);
									accountTradeResponse.setErrorMessage(baseResp.getErrorMessage());
									callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
									log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
									return accountTradeResponse;
								}
							}
						}
					}else{//非消费订单不允许撤单
						accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2012.getCode());
						accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2012.getName());
						//回写订单状态
						callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
						log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
						return accountTradeResponse;
					}
				}else{//订单状态非成功，无需撤单
					accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2011.getCode());
					accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2011.getName());
					//回写订单状态
					callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
					log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
					return accountTradeResponse;
				}
			}else{//原订单不存在
				accountTradeResponse.setReturnCode(TradeEventCodeEnum.TRADE_2010.getCode());
				accountTradeResponse.setErrorMessage(TradeEventCodeEnum.TRADE_2010.getName());
				//回写订单状态
				callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
				log.info("积分交易异常："+accountTradeResponse.getErrorMessage());
				return accountTradeResponse;
			}
		}else{//积分过期
			//后期根据实际情况实现
		}
		
		//第四步，回写订单状态
		callBackOrderStatus(createAccOrderResponse.getOrderOid(), orderStatus, accountTradeResponse.getErrorMessage());
		return accountTradeResponse;
	}
	
	/**
	 * 回写订单状态
	 * @param accountTradeResponse
	 * @param orderOid
	 * @param orderStatus
	 * @return
	 */
	private void callBackOrderStatus(String orderOid,
			String orderStatus, String errorMessage) {
		accOrderService.updateOrderStatus(orderOid, orderStatus, errorMessage);
	}
	
	/**
	 * 组装交易流水参数
	 * @param accountTradeRequest
	 * @param createAccountResponse
	 * @param accountType
	 * @param balance
	 * @return
	 */
	private AccountTransRequest installAccountTransRequest(AccountTradeRequest accountTradeRequest, String accountNo, String accountType, BigDecimal balance){
		//组装交易流水入参
		AccountTransRequest accountTransRequest = new AccountTransRequest();
		accountTransRequest.setTransAccountNo(accountNo);
		accountTransRequest.setAccountType(accountType);
		accountTransRequest.setBalance(balance);
		accountTransRequest.setDirection("add");
		accountTransRequest.setOrderDesc(accountTradeRequest.getOrderDesc());
		accountTransRequest.setOrderNo(accountTradeRequest.getOrderNo());
		accountTransRequest.setOrderPoint(accountTradeRequest.getBalance());
		accountTransRequest.setOrderType(accountTradeRequest.getOrderType());
		accountTransRequest.setRelationProductName(accountTradeRequest.getRelationProductName());
		accountTransRequest.setRelationProductNo(accountTradeRequest.getRelationProductNo());
		accountTransRequest.setRemark(accountTradeRequest.getRemark());
		accountTradeRequest.setRequestNo(accountTradeRequest.getRequestNo());
		accountTradeRequest.setSystemSource(accountTradeRequest.getSystemSource());
		accountTransRequest.setUserOid(accountTradeRequest.getUserOid());
		
		return accountTransRequest;
	}

	/**  
	* BigDecimal的加法运算。  
	* @param b1 被加数  
	* @param b2 加数  
	* @return 两个参数的和  
	*/  
	public BigDecimal add(BigDecimal b1,BigDecimal b2){
		return b1.add(b2);
	}   
	/**  
	* BigDecimal的减法运算。  
	* @param b1 被减数  
	* @param b2 减数 
	* @return 两个参数的差  
	*/  
	public BigDecimal sub(BigDecimal b1,BigDecimal b2){   
		return b1.subtract(b2);
	}
	/**
	 * 判断是否超额
	 * @param balance1
	 * @param balance2
	 * @return
	 */
	private boolean isExcess(BigDecimal balance1, BigDecimal balance2) {
		boolean isExcess = false;
		if(balance1.compareTo(balance2) != -1){//-1 小于  0 等于 1 大于
			isExcess = true;
		}
		return isExcess;
	}

}