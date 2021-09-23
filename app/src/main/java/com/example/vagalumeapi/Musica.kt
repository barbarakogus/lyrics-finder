package com.example.vagalumeapi

import com.google.gson.annotations.SerializedName

data class Musica(
    @SerializedName("name") val songName: String,
    @SerializedName("text") val lyrics: String)
