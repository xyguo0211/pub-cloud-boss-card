package com.cn.auth.config;


public enum AuthorityType {

	NON(0,""),

	QUERY(1, "查询"),

	ADD(2, "新增"),

	UPLOAD(3, "上传"),

	CANCEL(4, "取消"),

	CONFIRM(5, "审核"),

	UPDATE(7, "修改"),
	LOCK(8, "禁用"),
	RESET_PASSWORD(66,"重置密码"),
	VOUCHER_EXPORT(1021, "导出Excel"),

	SYNC(12, "同步"),

    DELETE(9, "删除"),


	UNLOCK(13, "解除锁定"),

	PEERREPLICATION(14, "同级复制"),

	ADDCHILD(15, "新增子角色"),

	EMPOWERMENT(16, "赋权"),
	SHIPDEST(17, "确认抵港"),

    MANDATORYSUBMIT(18, "打破规则提交数据"),
    CONFIRMGET(19, "确认收到"),
    CANCELGET(20, "取消收到"),
	EXPORTEXCEL(21,"导出excel"),
	AMOUNT_INOUT(22,"收到/付出"),
	ADVANCE(23,"预支"),
	CANCELADVANCE(24,"取消申请"),
	GENERATEPAYMENT(25,"生成付款单"),
	PAYMENT(26,"付款"),
	IMPORTREQUISITION(27,"引入应付款"),
	OUTPORTREQUISITION(28,"移出应付款"),
	DETAIL(29, "查看详情"),
	UPDATEVALIDITYDATE(30,"修改有效期"),
	DISPATCH(31,"加入调度"),
	CANCEL_DISPATCH(32,"取消调度"),
	MAST_SEND(33,"加入必送"),
	CANCEL_MAST_SEND(34,"取消必送"),
	VIEW_TASK(35,"查看任务详情"),
	EDIT_TASK(36,"编辑"),
	EDIT_REMARK(37,"更新备注"),
	HAND_UP(38,"挂起"),
	CANCEL_HAND_UP(39,"取消挂起"),
	EMPTYING(40,"放空"),
	CANCEL_EMPTYING(41,"取消放空"),
	SCHEDULE_LIST(42,"查询列表"),
	MERGE(43,"单拖套箱"),
	CANCEL_MERGE(44,"取消单拖套箱"),
	DOUBLE_DRAG(45,"双拖"),
	CANCEL_DOUBLE_DRAG(46,"取消双拖"),
	DOUBLE_MERGE(47,"双拖套箱"),
	CANCEL_DOUBLE_MERGE(48,"取消双拖套箱"),
	ASSIGN_DRIVER(49,"指派司机"),
	CANCEL_ASSIGN_DRIVER(50,"取消指派司机"),
	ASSIGN_CARTEAM(51,"指派车队"),
	CANCEL_ASSIGN_CARTEAM(52,"取消指派车队"),
	OVER_SCHEDULE(53,"结束行程"),
	EXPORT_TASK(54,"导出任务"),
	EXPORT_DRIVER(55,"导出司机信息"),
	EXPORT_PACKINGLIST(56,"导出装箱单"),
	GO_TO_ASSIGNED(57,"返回已指派"),
	INCIDENTAL_REGISTER(169, "杂费登记"),


	SETTLE(58,"加入结算"),
	CANCEL_SETTLE(59,"取消结算"),
	BATCHEXPORTEXCEL(60,"导出【单个】客户账单"),
	CUSTOMERQUOTELOG(61,"查询客户报价日志"),
	TRAILERQUOTELOG(62,"查询拖车报价日志"),
	NATIONALRATELOG(168,"查询全国费率维护操作日志"),
	CUSTOMER_QUOTE_COPY(63,"客户报价批量复制"),
	ENABLE(64,"启用"),
	DISABLE(65,"禁用"),
	OPENMANUAL(320,"人工分单"),
	OPENINTE(321,"智能分单"),

	COST_AUDITION(67,"费用预审核"),
	CUSTOMER_QUOTE_UPLOADEXCEL(68,"客户报价导入"),
	TRAILER_QUOTE_UPLOADEXCEL(68,"拖车报价导入"),
	PUBLISH_TASK(69,"发布任务" ),
	CUSTOMER_EXPORT(70,"下单客户导出"),
	QUERY_BASE_COST_LOG(71,"查询固定费用日志"),
	QUERY_OVER_TIME_TASK(72,"查询超时压夜任务"),
	EXPORT_CUSTOMERBILL(73,"导出【多个】客户账单"),
	EXPORT_OVER_TIME_TASK_LIST(74,"导出超时压夜任务"),

	INCOME(101,"现金收款"),
	COSTCHANGE(102,"费用转移"),
	COSTCHANGE2(103,"费用转移(同柜)"),
	IMPORTRECEIVABLE(104,"引入应收款"),
	OUTPORTRECEIVABLE(105,"移出应收款"),
	RECEIVABLES(106,"收款"),
	BATCH_DETAINING_CARGO(107,"批量扣货"),
	CANCEL_DETAINING_CARGO(108,"取消扣货"),
	UPDATE_FEES_MAINTAIN(109,"扣款维护"),
	INVOICE(110,"开票"),
	COLLECT_INVOICE(111,"收票"),
	FINANCIAL_AUDIT(112,"审核"),
	COST_DETAIL(113,"费用详情"),
	STATEMENT_DETAIL(114,"对账单明细"),
	DETAINING_CARGO_SET(115,"订舱单扣货设置"),
	BOOKSPACE_CABINET_STATUS(116,"订舱单柜状态"),
	EXPORT_STATEMENT(117,"导出对账单"),
	EDITRECEIVABLE(118,"编辑应收款"),
	CUSTOMER_ACCOUNT_WECHAT(119,"关联微信管理"),
	BACK_REQUISITION(303,"返回申请中"),
	//现结结算单tab页权限值
	VIEW_BY_DISPATCHER(200,"待调度核单查询"),
	VIEW_BY_FINANCE(201,"待子公司财务核单查询"),
	VIEW_BY_DIRECTOR(202,"待子公司总监核单查询"),
	VIEW_BY_CASHIER(203,"待出纳转账查询"),
	VIEW_FOR_PAID(204,"已支付查询"),
	ALL_CASH_SETTLE_TAB(205,"全部查询"),

	//现结结算单功能按扭权限值
	EXPORT_CASH_SETTLE_STATEMENT(206,"导出现结结算单"),
	VIEW_CASH_SETTLE_STATEMENT(207,"查看现结结算单"),
	PRINT(208,"打印"),
	EXAMINE_AND_VERIFY(209,"结算单审核"),
	PAY_BY_CASHIER(210,"总公司出纳转账"),
	APPLY_CASH_SETTLE(220,"申请现结"),
	COMPUTE_TOSEND(240,"改送费计算"),
    INVOICESUBMIT(241,"开票提交"),
	INVOICE_APPLY(221,"发票申请"),
	INVOICE_CONFIRMATION(222,"发票确认"),
	GENERATE_RECEIVABLE(223,"生成应收申请单"),
	RECEIVABLE_DETAIL(224,"申请单明细"),
	GEN_SETTLEMENT_RATE(225,"生成结算比例"),
	REVOKE_INVOICE_APPLY(226,"撤销发票申请"),
	SELECT_TIME(300, "输入时间"),
	REVOKE_AUDIT(301, "撤销审核"),

	FORBID_QUERY(400,"禁区查询"),
	FORBID_ADD(401,"禁区新增"),
	FORBID_EDIT(402,"禁区修改"),
	FORBID_DELETE(403,"禁区删除"),
	FORBID_EXPORT(404,"禁区导出"),
	FORBID_UPLOAD(405,"禁区导入"),

	VISITANT_QUERY(500,"查看访客列表"),
	VISITANT_EXPORT(501,"导出访客列表"),
	QUERY_DETAIL(502,"查看查价明细"),
	QUERY_DETAIL_EXPORT(503,"导出查价明细"),
	QUERY_STATISTICS(504,"查看查价统计"),
	QUERY_STATISTICS_EXPORT(505,"导出查价统计"),
	QUERY_VISIT_SET(506,"查看访问设置"),
	VISIT_SET(507,"修改访问设置"),
	CONTACT_CONSUMER(508,"联系客户"),
	FORBID_CONSUMER(509,"禁用/启用客户"),
	PRE_CONFIRMATION(520,"初步确认"),
	FINAL_CONFIRM(525,"最终导入"),

	IMPORT(600,"导入"),
	CHECKFEES(601,"提交初审"),
	IMPORTADJUSTCOST(602,"引入调整费"),
	BACKCHECKFEES(603,"撤销初审"),
	BACKBILLING(604,"撤销开票"),
	BACKINVOICESUBMIT(605,"撤销开票提交"),

	SPECIFY_CARTEAM(75,"指定车队"),
	QUERY_SPECIFY_CARTEAM_QUOTE(161,"查询指定拖车报价"),
	EDIT_SPECIFY_CARTEAM_QUOTE(162,"编辑指定拖车报价"),
	DELETE_SPECIFY_CARTEAM_QUOTE(163,"删除指定拖车报价"),
	QUERY_SPECIFY_CARTEAM_QUOTE_LOG(164,"查询指定拖车报价日志"),
	EXPORT_SPECIFY_CARTEAM_QUOTE(165,"导出指定拖车报价"),
	SPECIFY_TRAILER_QUOTE_UPLOADEXCEL(166,"指定拖车报价导入"),
	QUERY_RELEVANCE_CARTEAM_QUOTE(171,"查询关联拖车报价"),
	EDIT_RELEVANCE_CARTEAM_QUOTE(172,"编辑关联拖车报价"),
	DELETE_RELEVANCE_CARTEAM_QUOTE(173,"删除关联拖车报价"),
	QUERY_RELEVANCE_CARTEAM_QUOTE_LOG(174,"查询关联拖车报价日志"),
	EXPORT_RELEVANCE_CARTEAM_QUOTE(175,"导出关联拖车报价"),
	RELEVANCE_TRAILER_QUOTE_UPLOADEXCEL(176,"关联拖车报价导入"),
	APPLY_INVOICE(166,"申请开票"),
	INPUT_INVOICE(167,"录入发票"),
	PERSONALIZEDBILL(168,"设置个性化账单"),
	EXPORT_DRIVER_FREIGHT_STATEMENT(169,"导出账单"),
	FINANCIAL_RECHECK(120,"复审"),
	GET_ADJUST_ADVISE(181,"查看调整建议"),
	GET_REPLACE_DETAIL(183,"查看替换明细"),

	RECEIVED_DEPOSIT(710,"押金条收回"),
	APPLY_DEPOSIT_REFUND(720,"申请押金退款"),
	UPDATE_REFUND(730,"修改退款时间"),
	CHECK_RECEIVED_DEPOSIT(740,"确认收到退款"),

	QUERY_CUSTOMER_QUOTE_PRICE2(100, "查看客户报价"),
	;





//	VALIDATE_BY_DISPATCHER(209,"调度审核"),
//	VALIDATE_BY_FINANCE(210,"子公司财务核单审核"),
//	VALIDATE_BY_DIRECTOR(211,"子公司总监审核"),
//	PAY_BY_CASHIER(212,"总公司出纳转账");

	private int level;

	private String name;

	AuthorityType(int level, String name) {
		this.level = level;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public static String getNameByLevel(int level) {
		for (AuthorityType c : values()) {
			if (c.level==level) {
				return c.getName();
			}
		}
		return "";
	}

}
