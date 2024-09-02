package com.suatzengin.model

import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val id: Int,
    val auctionId: Int,
    val amount: Int,
    val timestamp: String,
    val bidderId: Int = 0,
)
