package com.cb.it.ebuy.coordinator

import com.cb.it.ebuy.order.domain.OrderBizType


data class CreateOrderCommand(
    val userId: Long,
    /*订单的业务类型：这一类订单中仅仅允许包含这一类商品*/
    val orderType: OrderBizType,
)

class OrderCreateHandler {
    fun createOrder() {
        /*1. check Inventory is available*/
        /*2. check .,..*/
        /*3. build inventory snapshot*/
        /*4. fee calculator: 计算费用, 确认要支付的余额*/
        /*5. create payment*/
        /*6. create order*/
    }
}