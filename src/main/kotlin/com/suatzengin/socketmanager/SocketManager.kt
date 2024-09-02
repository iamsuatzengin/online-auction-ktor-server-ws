package com.suatzengin.socketmanager

import com.suatzengin.controller.AuctionConnection
import com.suatzengin.controller.AuctionController
import com.suatzengin.model.Offer
import com.suatzengin.model.SocketResponseModel
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.coroutines.CoroutineContext

class SocketManager(
    private val controller: AuctionController,
    private val socket: DefaultWebSocketServerSession,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    val userConnection = AuctionConnection(socket)

    private val _socketEvent = MutableSharedFlow<SocketEvent>()
    private val socketEvent = _socketEvent.asSharedFlow()

    init {
        launch {
            socketEvent.collect { event ->
                when (event) {
                    is SocketEvent.Close -> close(code = event.code, message = event.message)
                    is SocketEvent.RemoveUserConnection -> controller.sessions.remove(event.connection)
                    is SocketEvent.SendData -> {
                        event.session.sendSerialized(event.data)
                    }
                }
            }
        }
    }

    suspend fun connect() = runCatching {
        val auctionId = socket.call.parameters["id"]?.toInt() ?: 0

        val sessions = controller.sessions

        sessions.add(userConnection)

        val auction = controller.getAuctionById(auctionId)

        sessions.last().session.sendAllDataToNewBidder()

        while (true) {
            if (controller.sessions.isEmpty()) {
                _socketEvent.emit(
                    SocketEvent.Close(code = CloseReason.Codes.NORMAL, message = "All bidder left the auction!")
                )
                return@runCatching
            }

            val offer = socket.receiveDeserialized<Offer>()

            val control = controller.control(
                offer = offer,
                startPrice = auction.startPrice,
                currentPrice = auction.currentPrice
            )

            val bidderCount = controller.sessions.size

            if (control) {
                val socketResponseModel = SocketResponseModel(
                    offerId = offer.id,
                    auctionId = offer.auctionId,
                    amount = offer.amount,
                    currentPrice = offer.amount,
                    timestamp = Instant.now().toString(),
                    isOfferApproved = true,
                    bidderCount = bidderCount
                )

                controller.offers.add(offer)
                auction.currentPrice = offer.amount

                sessions.forEach { session ->
                    _socketEvent.emit(
                        SocketEvent.SendData(
                            session = session.session,
                            data = socketResponseModel
                        )
                    )
                }
            } else {
                val currentSession = sessions.first { it == userConnection }

                val socketResponseModel = SocketResponseModel(
                    offerId = offer.id,
                    auctionId = offer.auctionId,
                    amount = offer.amount,
                    currentPrice = auction.currentPrice,
                    timestamp = Instant.now().toString(),
                    isOfferApproved = false,
                    warningMessage = "You cannot offer less than the current offer amount.",
                    bidderCount = bidderCount
                )

                _socketEvent.emit(
                    SocketEvent.SendData(
                        session = currentSession.session,
                        data = socketResponseModel
                    )
                )
            }
        }
    }.onFailure { throwable ->
        when (throwable) {
            is ClosedReceiveChannelException -> {
                _socketEvent.emit(SocketEvent.RemoveUserConnection(userConnection))
            }

            else -> {
                _socketEvent.emit(
                    SocketEvent.Close(
                        code = CloseReason.Codes.INTERNAL_ERROR,
                        message = throwable.message ?: "Unknown error!"
                    )
                )
            }
        }
    }

    private suspend fun WebSocketServerSession.sendAllDataToNewBidder() {
        if (controller.offers.isNotEmpty()) {
            controller.offers.forEach { offer ->
                val socketResponseModel = SocketResponseModel(
                    offerId = offer.id,
                    auctionId = offer.auctionId,
                    amount = offer.amount,
                    currentPrice = offer.amount,
                    timestamp = offer.timestamp,
                    isOfferApproved = true,
                    bidderCount = controller.sessions.size
                )

                sendSerialized(socketResponseModel)
            }
        }
    }

    private suspend fun close(code: CloseReason.Codes, message: String) {
        socket.close(CloseReason(code, message))
    }
}
