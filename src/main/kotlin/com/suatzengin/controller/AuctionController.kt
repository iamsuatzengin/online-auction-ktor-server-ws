package com.suatzengin.controller

import com.suatzengin.model.Auction
import com.suatzengin.model.Offer
import com.suatzengin.model.auctions
import java.util.*

class AuctionController {

    val sessions = Collections.synchronizedList<AuctionConnection>(ArrayList())

    val offers = mutableListOf<Offer>()

    fun getAuctions(): List<Auction> = auctions

    fun getAuctionById(id: Int): Auction = getAuctions().first { it.id == id }

    fun control(
        offer: Offer,
        startPrice: Int,
        currentPrice: Int,
    ): Boolean {
        val price = if(startPrice == currentPrice) startPrice else currentPrice

        return offer.amount > price
    }
}
