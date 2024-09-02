package com.suatzengin

import com.suatzengin.controller.AuctionController
import com.suatzengin.socketmanager.SocketManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Route.onlineAuctionRoute(
    auctionController: AuctionController
) {
    get("/auctions") {
        val list = auctionController.getAuctions()

        call.respond(
            status = HttpStatusCode.OK,
            message = list
        )
    }

    webSocket("/auctions/{id}") {
        val socketManager = SocketManager(
            controller = auctionController,
            socket = this
        )

        socketManager.connect()
    }
}
