package com.example.skgym.data

data class ProductCategory(
    val name: String,
    val image: String,
) {
    constructor() : this("", "")
}