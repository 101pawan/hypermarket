package com.hypermarket_android.dataModel

data class OrderInvoiceResponse(
    val message: String,
    val data: InvoiceResponse?
) {
    data class InvoiceResponse(val id:String,val order_id:String,val created_at:String,val total_payable_amount:String)
}



