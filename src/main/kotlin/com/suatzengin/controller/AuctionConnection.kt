package com.suatzengin.controller

import io.ktor.server.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class AuctionConnection(val session: WebSocketServerSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val id = lastId.incrementAndGet()
}
