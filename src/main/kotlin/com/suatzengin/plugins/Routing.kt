package com.suatzengin.plugins

import com.suatzengin.controller.AuctionController
import com.suatzengin.onlineAuctionRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val auctionController = AuctionController()

    routing {
        onlineAuctionRoute(auctionController = auctionController)
    }
}
