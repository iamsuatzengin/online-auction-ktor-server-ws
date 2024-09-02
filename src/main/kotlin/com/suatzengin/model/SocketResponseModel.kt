package com.suatzengin.model

import kotlinx.serialization.Serializable

@Serializable
data class SocketResponseModel(
    val offerId: Int,
    val auctionId: Int,
    val amount: Int,
    val currentPrice: Int,
    val timestamp: String,
    val isOfferApproved: Boolean,
    val warningMessage: String? = null,
    val bidderCount: Int,
)
