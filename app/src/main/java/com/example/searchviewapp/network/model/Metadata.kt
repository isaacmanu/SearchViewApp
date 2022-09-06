package com.example.searchviewapp.network.model

data class Metadata(
    val data_version: String,
    val match_id: String,
    val participants: List<String>
)