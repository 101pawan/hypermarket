package com.hypermarket_android.dataModel

data class NotificationResponse(
    val message: String,
    val result: List<Result>,
    val status: Int
) {
    data class Result(
        val created_at: String,
        val date: String,
        val id: Int,
        val is_read: Int,
        val notification: String,
        val notification_type: Int,
        val order_id: Any,
        val reciever_id: Int,
        val sender_id: Int,
        val title: String,
        val updated_at: String,
        val notificationDate: Long
    )
}