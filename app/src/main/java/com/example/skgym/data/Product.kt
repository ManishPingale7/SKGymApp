package com.example.skgym.data

data class Product(
    var name: String,
    var desc: String,
    var price: String,
    var category: String,
    var productImages: ArrayList<String>,
    var flavours: ArrayList<String>?=null,
    var key:String?=null
) {
    constructor() : this("", "", "", "", ArrayList<String>(), ArrayList<String>(),"")
}