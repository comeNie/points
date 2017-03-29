define(function () {
    this.host = '';
    return {
        host: this.host,
        api: {
            pointGoods: {
                save: this.host + '/points/goods/save',
                update: this.host + '/points/goods/update',
                edit: this.host + '/points/goods/edit',
                list: this.host + '/points/goods/page'
            },
            pointSetting: {
                save: this.host + '/points/setting/save',
                update: this.host + '/points/setting/update',
                edit: this.host + '/points/setting/edit',
                list: this.host + '/points/setting/page'
            },
            purchaseBill: {
                list: this.host + '/points/purchaseBill/page'
            },
            exchangedBill: {
                list: this.host + '/points/exchangedBill/page'
            },
            deliveryManage: {
                list: this.host + '/points/deliveryManage/page',
                findById: this.host + '/points/deliveryManage/findById',
                save: this.host + '/points/deliveryManage/save',
                cancel: this.host + '/points/deliveryManage/cancel'
            },
            userPoints: {
                list:this.host +'/points/userPoints/page'
            }
        }
    }
})