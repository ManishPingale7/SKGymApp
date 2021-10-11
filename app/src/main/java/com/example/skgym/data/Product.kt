package com.example.skgym.data

data class Product(
    var name: String,
    var desc: String,
    var price: String,
    var category: String,
    var productImage: String,
    var flavours: java.util.ArrayList<String>? = null,
    var key: String? = null
) {
    constructor() : this("", "", "", "", "", ArrayList<String>(), "")
}