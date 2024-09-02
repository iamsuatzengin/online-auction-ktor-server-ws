package com.suatzengin.model

import kotlinx.serialization.Serializable

@Serializable
data class Auction(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val startPrice: Int,
    var currentPrice: Int,
    val startTime: String,
    val endTime: String,
)

val auctions: List<Auction> = listOf(
    Auction(
        id = 1,
        title = "Antika Masa Saati",
        description = "19. yüzyıldan kalma, güzel bir antika masa saati. Çalışır durumda ve iyi korunmuş.",
        imageUrl = "https://www.invaluable.com/blog/wp-content/uploads/sites/77/2020/02/antique-clocks.jpg",
        startPrice = 100,
        currentPrice = 100,
        startTime = "2024-08-30T10:00:00",
        endTime = "2024-09-06T18:00:00"
    ),
    Auction(
        id = 2,
        title = "Nadir Kitap Koleksiyonu",
        description = "İçinde bir dizi nadir ve tarihi kitap bulunan koleksiyon. Kitaplar iyi durumda ve ciltlenmiş.",
        imageUrl = "https://fsuspecialcollections.wordpress.com/wp-content/uploads/2015/09/rarebooks.jpg",
        startPrice = 500,
        currentPrice = 500,
        startTime = "2024-08-31T09:00:00",
        endTime = "2024-09-07T21:00:00"
    ),
    Auction(
        id = 3,
        title = "Vintage Şarap Şişesi",
        description = "Ünlü bir şarap üreticisinden vintage bir şişe. Şarap 20 yıl yaşlı ve iyi korunmuş.",
        imageUrl = "https://assets.bwbx.io/images/users/iqjWHBFdfxIU/iRSu5M7T_Kwg/v0/-1x-1.jpg",
        startPrice = 150,
        currentPrice = 150,
        startTime = "2024-09-01T11:00:00",
        endTime = "2024-09-08T17:00:00"
    )
)
