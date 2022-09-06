package com.example.searchviewapp.network.model

data class Participant(
    val augments: List<String>,
    val `companion`: Companion,
    val gold_left: Int,
    val last_round: Int,
    val level: Int,
    val placement: Int,
    val players_eliminated: Int,
    val puuid: String,
    val time_eliminated: Double,
    val total_damage_to_players: Int,
    val traits: List<Trait>,
    val units: List<Unit>
)