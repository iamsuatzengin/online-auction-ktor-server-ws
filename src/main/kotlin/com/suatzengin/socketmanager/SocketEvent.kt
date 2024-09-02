package com.suatzengin.socketmanager

import com.suatzengin.controller.AuctionConnection
import com.suatzengin.model.SocketResponseModel
import io.ktor.server.websocket.*
import io.ktor.websocket.*

sealed interface SocketEvent {
    data class SendData(
        val session: WebSocketServerSession,
        val data: SocketResponseModel
    ) : SocketEvent

    data class RemoveUserConnection(
        val connection: AuctionConnection
    ) : SocketEvent

    data class Close(
        val code: CloseReason.Codes,
        val message: String
    ) : SocketEvent
}
