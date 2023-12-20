package com.example.bookyourtrain20

data class Order(
    var id: String = "",
    var userID: String = "",
    var travelID: String = "",
    var passengerName: String = "",
    var date: String = "",
    var trainClass: String = "",
    var additionalFacilities: String = ""
)
