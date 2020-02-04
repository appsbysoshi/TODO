package com.soshi.todo

data class Item (
    var id: Int = -1,
    var content: String = "",
    var isActive: Boolean = true
)