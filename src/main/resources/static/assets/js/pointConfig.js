define(function () {
    this.host = '';
    return {
        host: this.host,
        api: {
            pointGoods: {
                save: this.host + '/points/goods/save',
                update: this.host + '/points/goods/update',
                edit: this.host + '/points/goods/edit',
                list: this.host + '/points/goods/pag'
            },
            purchaseBill: {
                list: this.host + '/points/purchaseBill/page'
            },
            exchangedBill: {
                list: this.host + '/points/exchangedBill/page'
            }
        }
    }
})