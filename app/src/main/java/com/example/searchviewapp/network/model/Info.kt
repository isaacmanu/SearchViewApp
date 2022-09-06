package com.example.searchviewapp.network.model

data class Info(
    val game_datetime: Long,
    val game_length: Double,
    val game_version: String,
    val participants: List<Participant>,
    val queue_id: Int,
    val tft_game_type: String,
    val tft_set_core_name: String,
    val tft_set_number: Int
)