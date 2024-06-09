package com.example.randevusistemi

import java.util.Date

data class Book(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val isBorrowed: Boolean = false,
    val borrowedBy: String? = null,
    val borrowDate: Date? = null,
    val dueDate: Date? = null,
    val penalty: Int? = 0
)