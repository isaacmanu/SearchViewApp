package com.example.searchviewapp.network.model

data class Unit(
    val character_id: String,
    val itemNames: List<String>,
    val items: List<Int>,
    val name: String,
    val rarity: Int,
    val tier: Int
)