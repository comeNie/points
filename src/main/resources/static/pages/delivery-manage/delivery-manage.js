define([
    'http',
    'config',
    'pointConfig',
    'util',
    'extension'
], function (http, config, pointConfig, util, $$) {
    return {
        name: 'delivery-manage',
        init: function () {
            // 分页配置
            var pageOptions = {
                page: 1,
                rows: 10,
                state: "",
                startTime: '',
                endTime: ""
            };

            // 初始化数据表格
            var tableConfig = {
                ajax: function (origin) {
                    http.post(pointConfig.api.deliveryManage.list, {
                        data: {
                            page: pageOptions.page,
                            rows: pageOptions.rows,
                            state: pageOptions.state,
                            startTime: pageOptions.startTime,
                            endTime: pageOptions.endTime
                        },
                        contentType: 'form'
                    }, function (rlt) {
                        origin.success(rlt)
                    })
                },
                pageNumber: pageOptions.page,
                pageSize: pageOptions.rows,
                pagination: true,
                idField: 'oid',
                sidePagination: 'server',
                pageList: [10, 20, 30, 50, 100],
                queryParams: getQueryParams,
                onLoadSuccess: function () {
                },
                columns: [
                    {
                        width: 15,
                        align: 'center',
                        formatter: function (val, row, index) {
                            return index + 1
                        }
                    },
                    {
                        width: 20,
                        field: 'orderNumber'

                    }, {
                        width: 50,
                        field: 'userName'

                    }, {
                        width: 150,
                        field: 'address'

                    }, {
                        width: 50,
                        field: 'orderedTime'
                    }, {
                        width: 50,
                        field: 'goodsName'

                    }, {
                        width: 30,
                        field: 'goodsCount'

                    }, {
                        width: 50,
                        field: 'sendTime'
                    }, {
                        width: 50,
                        field: 'sendOperater'
                    }, {
                        width: 50,
                        field: 'logisticsCompany'
                    }, {
                        width: 50,
                        field: 'logisticsNumber'
                    }, {
                        width: 50,
                        field: 'state',
                        formatter: function (val, row, index) {
                            var state = parseInt(row.state);
                            switch (state) {
                                case 0:
                                    return '未发货';
                                case 1:
                                    return '已发货';
                                case 2:
                                    return '已取消';
                                default:
                                    return '-'
                            }
                        }
                    }, {
                        width: 50,
                        field: 'cancelReason'
                    }, {
                        width: 50,
                        field: 'cancelOperater'
                    }, {
                        width: 50,
                        field: 'cancelTime'
                    }, {
                        width: 100,

                        formatter: function (val, row, index) {

                            if (row.state == 2) {
                                return "-";
                            }
                            var buttons = [{
                                text: '发货',
                                type: 'button',
                                class: 'sendGoods',
                                isRender: row.state == 0
                            }, {
                                text: '取消',
                                type: 'button',
                                class: 'del',
                                isRender: row.state == 0
                            }, {
                                text: '编辑',
                                type: 'button',
                                class: 'edit',
                                isRender: row.state == 1
                            }];
                            return util.table.formatter.generateButton(buttons, "goods_Table");
                        },
                        events: {
                            'click .sendGoods': function (e, value, row) {
                                http.post(pointConfig.api.deliveryManage.findById, {
                                    data: {
                                        oid: row.oid
                                    },
                                    contentType: 'form'
                                    // async:'false',
                                }, function (result) {
                                    $('#deliveryManageForm').clearForm().find('input[type=hidden]').val('');
                                    $(document.coupon1Form).validator('destroy');
                                    util.form.validator.init($(document.deliveryManageForm));

                                    $$.formAutoFix($('#deliveryManageForm'), result);
                                    $("input[name='sendOperater']").val(getCookie("sendOperater"));
                                    $("input[name='logisticsCompany']").val(getCookie("logisticsCompany"));
                                    $('#deliveryManageModal').modal({
                                        show: true,
                                        backdrop: 'static'
                                    }).find('.modal-title').html("新增物流发货记录");
                                });


                            },
                            'click .edit': function (e, value, row) {
                                $('#deliveryManageModal').modal({
                                    show: true,
                                    backdrop: 'static'
                                }).find(".modal-title").html("修改物流发货记录");
                                http.post(pointConfig.api.deliveryManage.findById, {
                                    data: {
                                        oid: row.oid
                                    },
                                    contentType: 'form'
                                }, function (result) {
                                    $$.formAutoFix($('#deliveryManageForm'), result);

                                })
                            },
                            'click .del': function (e, value, row) {
                                confirm.find('.popover-title').html('提示');
                                var html = "确定让这组红包失效？<hr style='width:200px; height:2px;border-top:2px solid  #C0C0C0;' /><font color='red'>失效后，买家无法再领取该卡券；您也不能继续编辑内容；买家之前已领到的红包；在有效期内还能继续使用。</font>"
                                confirm.find('p').html(html);
                                $$.confirm({
                                    container: confirm,
                                    trigger: this,
                                    accept: function () {
                                        http.post(config.api.deleteCoupon, {
                                            data: {
                                                oid: row.oid
                                            },
                                            contentType: 'form'
                                        }, function (res) {
                                            if (res.errorCode == 0) {
                                                confirm.modal('hide');
                                                $('#dataTable').bootstrapTable('refresh')
                                            } else {
                                                errorHandle(res);
                                            }
                                        })
                                    }
                                })
                            }
                        }
                    }
                ]
            };

            // 初始化数据表格
            $('#goods_Table').bootstrapTable(tableConfig);
            // 搜索表单初始化
            $$.searchInit($('#searchForm'), $('#goods_Table'));

            //搜索
            function getQueryParams(val) {
                var form = document.searchForm;
                $.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

                pageOptions.rows = val.limit;
                pageOptions.page = parseInt(val.offset / val.limit) + 1;
                return val
            }

            //清空
            $('#clear').on('click', function (e) {
                e.preventDefault();
                var sform = document.searchForm;
                util.form.reset($(sform));
                $('#goods_Table').bootstrapTable('refresh', pageOptions);
            });


            function setCookie(c_name, value, expiredays) {
                var exdate = new Date();
                exdate.setDate(exdate.getDate() + expiredays);
                document.cookie = c_name + "=" + escape(value) +
                    ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
            }

            function getCookie(c_name) {
                if (document.cookie.length > 0) {
                    c_start = document.cookie.indexOf(c_name + "=");
                    if (c_start != -1) {
                        c_start = c_start + c_name.length + 1;
                        c_end = document.cookie.indexOf(";", c_start);
                        if (c_end == -1) c_end = document.cookie.length;
                        return unescape(document.cookie.substring(c_start, c_end));
                    }
                }
                return ""
            }

            //提交或修改物流信息
            $("#submit1").on("click", function () {

                if (!$('#deliveryManageForm').validator('doSubmitCheck')) return;
                var rsr = {};
                rsr.oid = document.deliveryManageForm.oid.value;
                rsr.logisticsCompany = document.deliveryManageForm.logisticsCompany.value;
                rsr.logisticsNumber = document.deliveryManageForm.logisticsNumber.value;
                rsr.sendOperater = document.deliveryManageForm.sendOperater.value;
                setCookie("logisticsCompany", rsr.logisticsCompany, 1);// 物流公司
                setCookie("sendOperater", rsr.sendOperater, 1);// 发货人
                http.post(pointConfig.api.deliveryManage.save, {
                    data: rsr,
                    contentType: 'form'
                }, function (result) {
                    if (result.errorCode == 0) {
                        $('#deliveryManageModal').modal('hide');
                        $('#goods_Table').bootstrapTable('refresh');
                    }
                })
            });

        }
    }
})
