define([
	'http',
    'config',
    'pointConfig',
    'util',
    'extension'
], function(http, config, pointConfig, util, $$){
	return {
		name: 'pointSetting',
		init: function(){
			// 分页配置
            var pageOptions = {
                page: 1,
                rows: 10
            }
         // 初始化数据表格
            var tableConfig = {
				ajax: function(origin) {
					http.post(pointConfig.api.pointSetting.list, {
						data: pageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: pageOptions.page,
				pageSize: pageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: getQueryParams,
				onLoadSuccess: function() {},
				columns: [
				{
					width: 30,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				},
				{ 
					width: 50,
					// 产品名
					field: 'name'
					
				},
				{
					width: 50,
					// 积分
					field: 'points'
				},
				{
					width: 50,
					// 所需金额
					field: 'amount'
				},
				{
					width: 50,
					// 状态
					field: 'state',
					formatter: function(val, row, index) {
						var val = parseInt(val);
						switch(val) {
							case 0:
								return '未上架'
							case 1:
								return '已上架'
							case 2:
								return '已下架'
							default:
								return '-'
						}
					}
				},
				{
					width: 50,
					// 上架时间
					field: 'updateTime',
					formatter: function(value, row) {
						if(row.state == 1) {
							return value;
						}else{
							return '-';
						}
					}
				},
				{
					width:100,
					filed: 'update',
					title: '操作',
					formatter: function(value, row) {
						var res = '-';
						if(row.state == 0) {
							//未上架的积分产品可以删除，已上架的不能删除
							res = '<a href="javascript:void(0)" class="style-putOn"  data-toggle="modal">上架</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="style-delete"  data-toggle="modal">删除</a>';
						} 
						if(row.state == 1){
							res = '<a href="javascript:void(0)" class="style-pullOff"  data-toggle="modal">下架</a>';
						}
						if(row.state == 2){
							res = '<a href="javascript:void(0)" class="style-delete"  data-toggle="modal">删除</a>';
						}
						return res;
					},
					events: {
						'click .style-delete': function(e, val, row){
							//进入修改页面
							console.log('oid===>'+row.oid);
							var goodsOid = row.oid;
							var confirm = $('#confirmModal');
							confirm.find('.popover-title').html('提示');
							confirm.find('p').html('确定要删除该积分产品吗？');
							$('#tip_cancel').show();
							$$.confirm({
								container: confirm,
								trigger: this,
								accept: function(){
									http.get(pointConfig.api.pointSetting.edit, {
										data: {
											oid: goodsOid,
											state: -1
										}, 
									}, function(res){
											if(res.errorCode == 0){
												confirm.modal('hide');
												$('#setting_Table').bootstrapTable('refresh', pageOptions);
											}else{
												errorHandle(res);//??
											}
									})
								}
							});
						},
						'click .style-putOn': function (e, val, row) {
							//上架
							var goodsOid = row.oid;
							console.log('oid===>'+row.oid);
							var confirm = $('#confirmModal');
							confirm.find('.popover-title').html('提示');
							confirm.find('p').html('确定要上架吗？');
							$('#tip_cancel').show();
							$$.confirm({
								container: confirm,
								trigger: this,
								accept: function(){
									http.get(pointConfig.api.pointSetting.edit, {
										data: {
											oid: goodsOid,
											state: 1
										},	
									}, function(res){
											console.log('errorCode=== '+ res.errorCode);
											if(res.errorCode == 0){
												confirm.modal('hide');
												$('#setting_Table').bootstrapTable('refresh', pageOptions);
											}else{
												errorHandle(res);//??
											}
									})
								}
							});
						},
						'click .style-pullOff': function (e, val, row) {
							//下架
							var goodsOid = row.oid;
							var confirm = $('#confirmModal');
							confirm.find('.popover-title').html('提示');
							confirm.find('p').html('确定要下架吗？');
							$('#tip_cancel').show();
							$$.confirm({
								container: confirm,
								trigger: this,
								accept: function(){
									http.get(pointConfig.api.pointSetting.edit, {
										data: {
											oid: goodsOid,
											state: 2
										},	
									}, function(res){
											if(res.errorCode == 0){
												confirm.modal('hide');
												$('#setting_Table').bootstrapTable('refresh', pageOptions);
											}else{
												errorHandle(res);//??
											}
									})
								}
							});
						}
					}
				}
				]
			}
            
            // 初始化数据表格
            $('#setting_Table').bootstrapTable(tableConfig);
            // 搜索表单初始化
            $$.searchInit($('#searchForm'), $('#setting_Table'));
            
            //列表自带搜索条件
            function getQueryParams(val) {
				var form = document.searchForm
				$.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象
				pageOptions.rows = val.limit
				pageOptions.page = parseInt(val.offset / val.limit) + 1
				
				return val
			}
            
            //搜索
			$('#setting_search').on('click', function(e) {
				e.preventDefault()
				var sform = document.searchForm
				var data = util.form.serializeJson(sform)
				data.row = 10
				data.page = 1
				pageOptions = data
				$('#setting_Table').bootstrapTable('refresh', pageOptions);
			});
			
			// 点击新增事件
			$('#setting_add').on('click', function() {
				$('#addSettingModal').modal('show');
			});
			
			//新增保存按钮事件
			$('#doAddSetting').on('click', function() {
				var name = $("#add_name").val().trim();
				var points = $("#add_points").val().trim();
				var amount = $("#add_amount").val().trim();
				var totalCount = $("#add_totalCount").val().trim();
				
				if (name == "") {
                    $("#add_name_err").text("积分名不能为空");
                    return;
                }
				if (points == "") {
                    $("#add_points_err").text("积分数不能为空");
                    return;
                }
				if (amount == "") {
					$("#add_amount_err").text("金额不能为空");
					return;
				}
				if (totalCount == "") {
                    $("#add_totalCount_err").text("积分总张数不能为空");
                    return;
                }
				
				var form = document.addSettingForm;
				$(form).ajaxSubmit({
					type: 'post',
					url: pointConfig.api.pointSetting.save,
					success: function() {
						util.form.reset($(form))
						$('#addSettingModal').modal('hide');
						$('#setting_Table').bootstrapTable('refresh', pageOptions);
					}
				})

			});
			
			
		}
	}
})