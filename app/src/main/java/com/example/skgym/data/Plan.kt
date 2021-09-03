package com.example.skgym.data

data class Plan(
    val name: String,
    val desc: String,
    val timeNumber: String,
    val timetype: String,
    val fees: String
) {
    constructor() : this("", "", "", "", "")
}