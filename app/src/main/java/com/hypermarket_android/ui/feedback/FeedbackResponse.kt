package com.hypermarket_android.ui.feedback

data class FeedbackResponse(
    val `data`: Data,
    val message: String
)

data class Data(
    val created_at: String,
    val id: Int,
    val message: String,
    val updated_at: String,
    val user_id: Int
)