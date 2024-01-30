package com.cb.it.ebuy.order.domain


data class Rmb(
    val value: Long
) {
    companion object {
        fun of(value: Long): Rmb {
            return Rmb(value)
        }
    }
}

enum class OrderBizType(
    val code: Int,
    val desc: String
) {
    NORMAL(1, "普通实体商品订单"),
    VIP(2, "VIP 订单"),
    CLASS(3, "知识付费课程 订单"),
}

data class Order(
    /*自增id*/
    val id: Long,

    /*业务上的唯一id, 由3部分组成userId-time-xxx，这样天然有一定能力防止刷单*/
    val orderNo: String,

    /*商品类型*/
    val orderType: OrderBizType,
)

enum class PromotionType {
    /**
     * 首件商品优惠
     */
    FIRST_INVENTORY,

    /**
     * 满减优惠
     */
    FULL_CUT,
}

data class FirstInventoryPromotion(
    val inventoryId: Int,
    val promotionId: Long,
    val promotionType: PromotionType,
)


data class OrderInventoryPromotionSnap(
    val fp: FirstInventoryPromotion,
)


enum class PaymentType {
    ALI_PAY,
    IAP,
    WECHAT_PAY,
}


data class OrderPayment(
    val payNo: String,
    val orderId: String,
    val type: PaymentType,
    /*支付的金额*/
    val paidPrice: Rmb,
)


/**
 * 费用来源
 *
 * 1. 三方支付
 * 2. 后台发放
 * 3. 用户余额
 * 4. 用户积分兑换
 * 5. 后台直接发放
 */
data class OrderFee(
    val mainPaymentId: String,
    val payment: OrderPayment,
)

/**
 * 用于冗余订单中需要的快照信息
 */
data class OrderInventorySnap(
    val inventoryId: Long,
    val inventoryNum: Int,
    /*序列化一个当前的 json 字符串存储 所有的属性*/
    val detail: String,
    val price: Rmb, /*单价*/
    val totalPrice: Rmb, /*总价 */

    /*支持首单优惠*/

)

