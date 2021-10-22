package com.example.skgym.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlansDB(
    val plan: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)